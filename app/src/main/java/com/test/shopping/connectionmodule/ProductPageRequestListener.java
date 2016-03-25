package com.test.shopping.connectionmodule;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.VolleyError;
import com.test.shopping.BuildConfig;
import com.test.shopping.model.CacheUtil;
import com.test.shopping.model.ProductDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sujoy on 3/23/16.
 */
public class ProductPageRequestListener implements com.android.volley.Response.Listener, com.android.volley.Response.ErrorListener {

    private Context mContext;

    private WebHandlerRequestCallback mDataCallback;

    // Product based tags as returned by Product Page API

    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PRODUCT_ID = "productId";
    private static final String TAG_PRODUCT_NAME = "productName";
    private static final String TAG_SHORT_DESC = "shortDescription";
    private static final String TAG_LONG_DESC = "longDescription";
    private static final String TAG_PRICE = "price";
    private static final String TAG_PROD_IMAGE = "productImage";
    private static final String TAG_REVIEW_RATING = "reviewRating";
    private static final String TAG_REVIEW_COUNT = "reviewCount";
    private static final String TAG_IN_STOCK = "inStock";

    private static final String TAG_TOTAL_PRODUCTS = "totalProducts";
    private static final String TAG_PAGE_NUMBER = "pageNumber";
    private static final String TAG_PAGE_SIZE = "pageSize";
    private static final String TAG_STATUS = "status";
    private static final String TAG_KIND = "kind";
    private static final String TAG_ETAG = "etag";


    private static final String TAG = "ProductPageListener";

    public ProductPageRequestListener(Context context, WebHandlerRequestCallback productResponse) {
        mContext = context;
        mDataCallback = productResponse;
    }

    @Override
    public void onResponse(Object response) {
        if(BuildConfig.DEBUG)Log.d(TAG, "onResponse..response:" + response);

        if(response != null) {
            String resp = (String)response;
            try {
                JSONObject jsonObj = new JSONObject(resp);
                JSONArray products = jsonObj.getJSONArray(TAG_PRODUCTS);
                //If product element is not empty then update the app caches with the product details
                if (products != null && products.length() > 0) {

                    //Loop through all the products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        String productName = null;
                        String shortDesc = null;
                        String longDesc = null;
                        String price = null;
                        String image = null;
                        double rating = 0;
                        int count = 0;
                        boolean inStock = false;

                        String productId = c.getString(TAG_PRODUCT_ID);
                        if(productId != null) {
                            if(c.has(TAG_PRODUCT_NAME)) {
                                productName = c.getString(TAG_PRODUCT_NAME);
                            }
                            if(c.has(TAG_SHORT_DESC)) {
                                shortDesc = c.getString(TAG_SHORT_DESC);
                            }
                            if(c.has(TAG_LONG_DESC)) {
                                longDesc = c.getString(TAG_LONG_DESC);
                            }
                            if(c.has(TAG_PRICE)) {
                                price = c.getString(TAG_PRICE);
                            }
                            if(c.has(TAG_PROD_IMAGE)) {
                                image = c.getString(TAG_PROD_IMAGE);
                            }
                            rating = c.getDouble(TAG_REVIEW_RATING);
                            count = c.getInt(TAG_REVIEW_COUNT);
                            inStock = c.getBoolean(TAG_IN_STOCK);

                            ProductDataModel product = new ProductDataModel(productId,
                                    productName,
                                    shortDesc,
                                    longDesc,
                                    price,
                                    image,
                                    rating,
                                    count,
                                    inStock);


                            CacheUtil.getInstance().addProductId(productId);
                            /*
                             * Insert Product details in Product Cache
                             */
                            if (product != null) {
                                CacheUtil.getInstance().addProduct(productId, product);
                            }
                        }
                    }
                }

                int pageNumber = jsonObj.getInt(TAG_PAGE_NUMBER);
                ConnectionUtil.getInstance(mContext).updateCurrent(pageNumber);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mDataCallback.updateData();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if(BuildConfig.DEBUG)Log.d(TAG, "onErrorResponse..volleyError:"+volleyError.networkResponse.statusCode);
        mDataCallback.updateError();
    }
}
