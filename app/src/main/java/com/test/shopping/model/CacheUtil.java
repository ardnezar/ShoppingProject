package com.test.shopping.model;

import android.util.Log;
import android.util.LruCache;

import com.test.shopping.BuildConfig;

import java.util.ArrayList;

/**
 * Created by sd250307 on 3/23/16.
 *
 * This utility encapsulates  Product Id list and Product Item Cache
 */
public class CacheUtil {

    private static CacheUtil sInstance;
    private static final String TAG = "ShoppingCacheUtil";

    private CacheUtil(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mProductCache = new LruCache<String, ProductDataModel>(cacheSize) {

        };

        mProductIdList = new ArrayList<>();
    }

    private LruCache<String, ProductDataModel> mProductCache;

    private ArrayList<String> mProductIdList;

    public static synchronized CacheUtil getInstance() {
        if (sInstance == null) {
            sInstance = new CacheUtil();
        }
        return sInstance;
    }

    public void addProduct(String key, ProductDataModel product) {
        if (getProduct(key) == null) {
            mProductCache.put(key, product);
        }
    }

    public ProductDataModel getProduct(String key) {
        return mProductCache.get(key);
    }

    public void addProductId(String productId) {
//        if(BuildConfig.DEBUG)Log.d(TAG, "addProductId..Product key:"+productId+",mProductIdList.size():"+mProductIdList.size());
        mProductIdList.add(productId);
    }

    public String getProductId(int index) {
        return mProductIdList.get(index);
    }

    public int getProductListSize() {
        int count =  (mProductIdList != null ? mProductIdList.size():0);
//        if(BuildConfig.DEBUG)Log.d(TAG, "getProductListSize..count:"+count);
        return count;
    }



    public void cleanup() {
        if(mProductCache != null) {
            mProductCache.evictAll();
            mProductCache = null;
        }
        if(mProductIdList != null) {
            mProductIdList.trimToSize();
            mProductIdList = null;
        }
    }
}
