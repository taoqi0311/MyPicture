package com.synway.mypicture;


import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class SearchFiles {
    private Handler handler;

    public SearchFiles(Handler handler) {
        this.handler = handler;

    }

    public void start(String path) {
        Thread thread = new Thread(new SearchThread(handler, path));
        thread.start();

    }

    private class SearchThread implements Runnable {
        private String path;
        private Handler handler;

        private SearchThread(Handler handler, String path) {
            this.handler = handler;
            this.path = path;
        }

        @Override
        public void run() {


            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                ArrayList<MyBean> list = new ArrayList<>();
                for (File file1 : files) {
                    if (!file1.getName().startsWith(".")) {
                        MyBean bean = new MyBean();
                        bean.absloutePath = file1.getAbsolutePath();
                        bean.name = file1.getName();

                        if (file1.isDirectory()) {
                            bean.type = 0;
                            list.add(bean);
                        } else {
                            if (file1.length() > 1024*10) {
                                bean.type = 1;
                                list.add(bean);
                            }

                        }

                    }
                }
                Message message = handler.obtainMessage();

                if (list.size() > 0) {
                    Collections.sort(list);
                    message.obj = list;
                    message.what = MainActivity.UP_DATE;
                    handler.sendMessage(message);
                } else {
                    message.what = MainActivity.EMPTY;
                    handler.sendMessage(message);

                }

            }
        }
    }
}
