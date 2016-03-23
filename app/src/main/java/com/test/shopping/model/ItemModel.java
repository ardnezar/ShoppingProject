package com.test.shopping.model;

/**
 * Created by sujoy on 3/22/16.
 * This model describes each Item
 */
public class ItemModel {


    public String getProductId() {
        return mProductId;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getLongDescription() {
        return mLongDescription;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public float getReviewRating() {
        return mReviewRating;
    }

    public int getReviewCount() {
        return mReviewCount;
    }

    public boolean isInStock() {
        return mInStock;
    }

    private String mProductId;
    private String mProductName;
    private String mShortDescription;
    private String mLongDescription;
    private String mPrice;
    private String mProductImage;
    private float mReviewRating;
    private int mReviewCount;
    private boolean mInStock;

    public ItemModel(String productId,
                     String prodName,
                     String shortDes,
                     String longDes,
                     String price,
                     String image,
                     float rating,
                     int count,
                     boolean stock) {

    }
}
