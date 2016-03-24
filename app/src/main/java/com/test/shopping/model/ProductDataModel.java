package com.test.shopping.model;

/**
 * Created by sujoy on 3/22/16.
 * This model describes each Item
 */
public class ProductDataModel {


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

    public double getReviewRating() {
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
    private double mReviewRating;
    private int mReviewCount;
    private boolean mInStock;

    public ProductDataModel(String productId,
                            String prodName,
                            String shortDes,
                            String longDes,
                            String price,
                            String image,
                            double rating,
                            int count,
                            boolean stock) {
        mProductId = productId;
        mProductName = prodName;
        mShortDescription = shortDes;
        mLongDescription = longDes;
        mPrice = price;
        mProductImage = image;
        mReviewRating = rating;
        mReviewCount = count;
        mInStock = stock;
    }
}
