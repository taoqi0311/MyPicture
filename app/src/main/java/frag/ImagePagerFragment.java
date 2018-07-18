package frag;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.synway.mypicture.R;

import java.util.ArrayList;

import adapter.PhotoPagerAdapter;

public class ImagePagerFragment extends android.app.Fragment {

    public final static String ARG_PATH = "PATHS";
    public final static String ARG_CURRENT_ITEM = "ARG_CURRENT_ITEM";

    private ArrayList<String> paths;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem;

    public static ImagePagerFragment newInstance(ArrayList<String> paths, int currentItem, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {
        ImagePagerFragment f = new ImagePagerFragment();
        Bundle arguments = new Bundle();
        arguments.putStringArrayList(ARG_PATH, paths);
        arguments.putInt(ARG_CURRENT_ITEM, currentItem);
        f.setArguments(arguments);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paths = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<String> pathArr = bundle.getStringArrayList(ARG_PATH);
            paths.clear();
            if (pathArr != null) {
                paths.addAll(pathArr);
            }
            currentItem = bundle.getInt(ARG_CURRENT_ITEM);

        }

        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), paths);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_image_pager, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_photos);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(3);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        paths.clear();
        paths = null;
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onDestroy() {
        Glide.get(this.getContext()).clearMemory();
        super.onDestroy();
    }
}
