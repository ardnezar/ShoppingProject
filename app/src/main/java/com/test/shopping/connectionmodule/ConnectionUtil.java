package com.test.shopping.connectionmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.test.shopping.AppConstants;

/**
 * Created by sd250307 on 3/9/16.
 */
public class ConnectionUtil {
    private static ConnectionUtil sInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;
    private int mCurrentPage;
    private int mTotalProducts;

    public static final int MAX_PAGE_SIZE =  30;

    private ConnectionUtil(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(MAX_PAGE_SIZE);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
        mCurrentPage = 1;
    }

    public static synchronized ConnectionUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ConnectionUtil(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cleanup() {
        if(mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void sendProductRequest(WebHandlerRequestCallback callback) {
        String url = AppConstants.URL_BASE_DEBUG + AppConstants.PRODUCT_LIST;
        url = String.format(url, AppConstants.API_KEY, mCurrentPage, MAX_PAGE_SIZE);
        ProductPageRequest request = new ProductPageRequest(
                url,
                new ProductPageRequestListener(mContext, callback),
                null);
        mRequestQueue.add(request);
    }


}