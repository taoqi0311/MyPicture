package util;

import android.util.Log;

/**
 * Created by dell on 2017/4/10.
 */

public class LogUtil {
    public static void log(String ...str){
        for (int i = 0; i < str.length; i++) {
            Log.i("tq",str[i]);

        }
    }
}
