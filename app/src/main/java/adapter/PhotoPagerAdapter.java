package adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.synway.mypicture.R;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ysm
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> paths = new ArrayList<>();
    private RequestManager mGlide;

    public PhotoPagerAdapter(RequestManager glide, List<String> paths) {
        this.paths = paths;
        this.mGlide = glide;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.picker_picker_item_page, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);

        final String singlepath = paths.get(position);
        final Uri uri;
        if (singlepath.startsWith("http")) {
            uri = Uri.parse(singlepath);
        } else {
            uri = Uri.fromFile(new File(singlepath));
        }
        mGlide.load(uri)
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .override(800, 800)
                .placeholder(R.mipmap.__picker_ic_photo_black_48dp)
                .error(R.mipmap.__picker_ic_broken_image_black_48dp)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing()) {
                        ((Activity) context).onBackPressed();
                    }
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                File file = new File(singlepath);
                if (file.exists()) {
                    try {
                        File copy = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/picture", file.getName() + ".jpg");
                        if (copy.exists()) {
                            Toast.makeText(context, "图片已经存在DCIM/picture中", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "图片已经保存在DCIM/picture中", Toast.LENGTH_SHORT).show();

                            if (!copy.getParentFile().exists()) {
                                copy.getParentFile().mkdirs();
                            }
                            RandomAccessFile copyFile = new RandomAccessFile(copy, "rw");
                            byte buf[] = new byte[(int) file.length()];
                            RandomAccessFile oriAccessFile = new RandomAccessFile(file, "rw");
                            oriAccessFile.read(buf);
                            copyFile.write(buf);
                            copyFile.close();
                            oriAccessFile.close();

                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            file = new File(copy.getAbsolutePath());
                            if (file.exists()) {
                                Uri uri = Uri.fromFile(file);
                                intent.setData(uri);
                                context.sendBroadcast(intent);
                            }
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }


}
