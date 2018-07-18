package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.synway.mypicture.R;

import java.io.File;
import java.util.ArrayList;

import event.OnPhotoClickListener;
import event.OnPhotoLongClikListner;

/**
 * Created by dell on 2017/4/10.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.MyHolder> {
    private Context context;
    private ArrayList<String> list;
    private RequestManager glide;
    private int imageSize;
    private OnPhotoClickListener onPhotoClickListener = null;
    private OnPhotoLongClikListner onPhotoLongClikListner;

    public PhotoGridAdapter(Context context, RequestManager glide) {
        this.glide = glide;
        this.context = context;
        this.list = new ArrayList<>();
        setColumnNumber(context);
    }

    public void setList(ArrayList<String> list) {
        this.list.clear();
        this.list.addAll(list);

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        File file = new File(list.get(position));

        glide.load(file)
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f).crossFade(10)
                .override(imageSize, imageSize)
                .placeholder(R.mipmap.__picker_ic_photo_black_48dp)
                .error(R.mipmap.__picker_ic_broken_image_black_48dp)
                .into(holder.ivPhoto);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoClickListener.onClick(v, position, list.get(position));
            }
        });

        holder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onPhotoLongClikListner.onLongClick(v, position, list.get(position));
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;

        public MyHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

    @Override
    public void onViewRecycled(MyHolder holder) {
        Glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }

    private void setColumnNumber(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / 4;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    public void setOnPhotoLongClickListener(OnPhotoLongClikListner onPhotoClickListener) {
        this.onPhotoLongClikListner = onPhotoClickListener;
    }
}
