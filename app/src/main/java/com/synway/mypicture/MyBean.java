package com.synway.mypicture;

/**
 * Created by dell on 2017/4/12.
 */

public class MyBean implements Comparable<MyBean> {
    public String absloutePath;
    public String name;
    public int type = 0;//0 文件夹  1 文件

    @Override
    public int compareTo(MyBean another) {
        return this.name.compareToIgnoreCase(another.name);
    }
}
