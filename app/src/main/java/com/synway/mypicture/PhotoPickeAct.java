package com.synway.mypicture;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;

import frag.ImagePagerFragment;
import frag.PhotoPickerFragment;

public class PhotoPickeAct extends Activity {
    public static final String PHOTO_DIC = "photo_dic";
    private FragmentManager manager;
    private ImagePagerFragment imagePagerFragment;
    public View emptyView;
    public static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_picke);
        emptyView = findViewById(R.id.empty_view);
        String stringExtra = getIntent().getStringExtra(PHOTO_DIC);
        String goal = Environment.getExternalStorageDirectory() + "/" + stringExtra;
        if (!new File(goal).exists()) {
            Toast.makeText(PhotoPickeAct.this, "文件夹不存在,请确认", Toast.LENGTH_SHORT).show();
            return;
        }
        manager = getFragmentManager();
        PhotoPickerFragment pickerFragment = (PhotoPickerFragment) manager.findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment.newInstance(goal, "");
            manager.beginTransaction().replace(R.id.container, pickerFragment, "tag").commit();
            manager.executePendingTransactions();
        }

    }

    @Override
    public void onBackPressed() {
        if (imagePagerFragment != null && imagePagerFragment.isVisible()) {
            if (manager.getBackStackEntryCount() >= 1) {
                manager.popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }


    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
        this.imagePagerFragment = imagePagerFragment;
        manager.beginTransaction()
                .replace(R.id.container, this.imagePagerFragment)
                .addToBackStack(null)
                .commit();
    }
}
