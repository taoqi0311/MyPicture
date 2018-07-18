package util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * 更改Glide的全局行为
 * Created by Mfh on 2017/1/13.
 */

public class CustomCachingGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); //byte级
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customMemoryCacheSize = (int) (0.6 * defaultMemoryCacheSize); //内存缓存大小
        int customBitmapPoolSize = (int) (0.6 * defaultBitmapPoolSize); //bitmap pool大小

        glideBuilder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        glideBuilder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
