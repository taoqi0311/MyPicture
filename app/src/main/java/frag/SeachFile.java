package frag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


public class SeachFile {
    private OnResultListenr listenr;
    private String dest;

    public SeachFile(String dest) {
        this.dest = dest;

    }

    public void start() {
        if (listenr != null) {
            new Thread(new MyRunaable()).start();
        }
    }

    public void stop() {
        if (listenr != null)
            listenr = null;
    }

    private class MyRunaable implements Runnable {
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            File file = new File(dest);
            File[] files = file.listFiles();
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                String absolutePath = files[i].getAbsolutePath();
                File zifile = new File(absolutePath);
                if (zifile.canRead() && zifile.isFile() && zifile.length() >= 16)
                    if (!zifile.getName().startsWith("."))
                        arrayList.add(absolutePath);
            }
            if (listenr != null)
                listenr.result(arrayList);

        }
    }

    public void setListenr(OnResultListenr listenr) {
        this.listenr = listenr;
    }

    interface OnResultListenr {
        void result(ArrayList<String> list);
    }

}
