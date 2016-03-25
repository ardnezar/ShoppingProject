package com.test.shopping.model;

import android.util.Log;
import android.util.LruCache;

import com.test.shopping.BuildConfig;

import java.util.ArrayList;

/**
 * Created by sujoy on 3/23/16.
 *
 * This singleton utility encapsulates Product Id list and Product Item Cache
 */
public class CacheUtil {

    private static CacheUtil sInstance;
    private static final String TAG = "ShoppingCacheUtil";

    private CacheUtil(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mProductCache = new LruCache<String, ProductDataModel>(cacheSize) {};

        mProductIdList = new ArrayList<>();
    }

    //An LRU Cache to store objects associated with productIds
    private LruCache<String, ProductDataModel> mProductCache;


    //A list to store the ProductIds
    private ArrayList<String> mProductIdList;

    public static CacheUtil getInstance() {
        if (sInstance == null) {
            synchronized (CacheUtil.class) {
                /*
                 * Additional validation before creating the static instance is required, as the
                  * previous thread might have already initialized this instance
                 */

                if (sInstance == null) {
                    sInstance = new CacheUtil();
                }
            }
        }
        return sInstance;
    }

    /*
     * This method is used to insert a product object in the cache
     */
    public void addProduct(String key, ProductDataModel product) {
        if (getProduct(key) == null) {
            mProductCache.put(key, product);
        }
    }

    private ProductDataModel getProduct(String key) {
        return mProductCache.get(key);
    }


    /*
     * This method is used to get the product object associated with a product Id that is located
     * in the given position in the productId list
     */
    public ProductDataModel getProduct(int position) {
        String key = mProductIdList.get(position);
        return mProductCache.get(key);
    }

    /*
     * This method is used to add productId to the productId list
     */
    public void addProductId(String productId) {
        mProductIdList.add(productId);
    }

    /*
     * Get productId associated with the position in the product Id list
     */

    public String getProductId(int index) {
        return mProductIdList.get(index);
    }


    /*
     * This method is used to get the total size of the product list
     */
    public int getProductListSize() {
        int count =  (mProductIdList != null ? mProductIdList.size():0);
        return count;
    }


    /*
     * This method is used to cleanup the caches maintained by the app
     */
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
