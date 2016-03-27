package com.test.shopping.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.LruCache;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sujoy on 3/23/16.
 *
 * This singleton utility encapsulates Product Id list and Product Item Cache
 */
public class CacheUtil {

    private static CacheUtil sInstance;
    private static Context mContext;
    private static final String TAG = "ShoppingCacheUtil";
    public static final String SORT_TYPE_KEY = "sort_type";

    public static final int SORT_NAME = 0;
    public static final int SORT_RATING = 1;
    public static final int SORT_REVIEW_COUNT = 2;
    public static final int SORT_PRICE = 3;

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

    public static CacheUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (CacheUtil.class) {
                /*
                 * Additional validation before creating the static instance is required, as the
                  * previous thread might have already initialized this instance
                 */

                if (sInstance == null) {
                    sInstance = new CacheUtil();
                    mContext = context;
                }
            }
        }
        return sInstance;
    }

    public void updateProductIdList() {
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int sortType = pref.getInt(SORT_TYPE_KEY, SORT_NAME);
        if(sortType == SORT_NAME) {
            Collections.sort(mProductIdList, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return mProductCache.get(s).getProductName().compareTo(mProductCache.get(t1).getProductName());
                }
            });
        } else if(sortType == SORT_RATING) {
            try {
                Collections.sort(mProductIdList, new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        return (mProductCache.get(s).getReviewRating() >= mProductCache.get(t1).getReviewRating() ? -1 : 1);
                    }
                });
            } catch(Exception ex){}
        } else if(sortType == SORT_REVIEW_COUNT) {
            Collections.sort(mProductIdList, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return (mProductCache.get(s).getReviewCount() >= mProductCache.get(t1).getReviewCount() ? -1 : 1);
                }
            });
        } else if(sortType == SORT_PRICE) {
            Collections.sort(mProductIdList, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    try {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        Number n1 = format.parse(mProductCache.get(s).getPrice());
                        Number n2 = format.parse(mProductCache.get(t1).getPrice());
                        return (n1.floatValue() <= n2.floatValue() ? -1 : 1);
                    } catch (Exception ex){
                        return -1;
                    }
                }
            });
        }
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
