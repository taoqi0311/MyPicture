package frag;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.synway.mypicture.PhotoPickeAct;
import com.synway.mypicture.R;

import java.io.File;
import java.util.ArrayList;

import adapter.PhotoGridAdapter;
import event.OnPhotoClickListener;
import event.OnPhotoLongClikListner;

public class PhotoPickerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String gogal;
    private RequestManager mGlideRequestManager;
    private PhotoGridAdapter photoGridAdapter;
    private SeachFile seachFile;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ImagePagerFragment imagePagerFragment;

    public static PhotoPickerFragment newInstance(String param1, String param2) {
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gogal = getArguments().getString(ARG_PARAM1);
        }

        setRetainInstance(true);
        mGlideRequestManager = Glide.with(this);
        photoGridAdapter = new PhotoGridAdapter(getContext(), mGlideRequestManager);
        seachFile = new SeachFile(gogal);
        seachFile.setListenr(listenr);
        seachFile.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_photo_picker, container, false);
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.recycle);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v, int position, String path) {
                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                if (arrayList.size() >= 1) {
                    imagePagerFragment = ImagePagerFragment.newInstance(arrayList, position, screenLocation, v.getWidth(), v.getHeight());
                    ((PhotoPickeAct) getActivity()).addImagePagerFragment(imagePagerFragment);
                }
            }
        });

        photoGridAdapter.setOnPhotoLongClickListener(new OnPhotoLongClikListner() {
            @Override
            public void onLongClick(View v, int position, String path) {
                File file = new File(path);
                if (file.delete()) {
                    arrayList.remove(path);
                    photoGridAdapter.setList(arrayList);
                    photoGridAdapter.notifyDataSetChanged();
                }
                Toast.makeText(getActivity(), "已删除", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > 30) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    mGlideRequestManager.resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mGlideRequestManager.resumeRequests();
                }
            }
        });


        return rootview;

    }

    private SeachFile.OnResultListenr listenr = new SeachFile.OnResultListenr() {
        @Override
        public void result(ArrayList<String> list) {
            PhotoPickeAct act = (PhotoPickeAct) getActivity();
            if (list != null && list.size() > 0) {
                photoGridAdapter.setList(list);
               act.handler.post(new Runnable() {
                   @Override
                   public void run() {
                  photoGridAdapter.notifyDataSetChanged();
                   }
               });
            } else {

                act.emptyView.setVisibility(View.VISIBLE);
            }
            arrayList.clear();
            arrayList.addAll(list);
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(this.getContext()).clearMemory();
        seachFile.stop();
    }
}
