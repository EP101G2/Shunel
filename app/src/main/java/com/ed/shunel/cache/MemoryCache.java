package com.ed.shunel.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache{
    private LruCache<Integer, Bitmap> mMemoryCache;


    public MemoryCache(){
        //初始化Lru快取
        initImageCache();
    }



    private void initImageCache() {
        //計算可使用的最大記憶體
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //取四分之一可使用的記憶體作為快取
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }



    @Override
    public Bitmap get(int isbn) {
        return mMemoryCache.get(isbn);
    }

    @Override
    public void put(int isbn, Bitmap bmp) {
        mMemoryCache.put(isbn, bmp);
    }

    @Override
    public void ifExistDelete(int isbn) {
        Bitmap LruBitmap = mMemoryCache.get(isbn);
        if(LruBitmap!=null){
            mMemoryCache.remove(isbn);
        }
    }


}
