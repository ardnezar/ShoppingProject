package com.test.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.test.shopping.R;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.model.CacheUtil;
import com.test.shopping.model.ProductDataModel;
import com.test.shopping.view.ProductDetailActivity;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by sujoy on 3/22/16.
 * Adapter to show product listing in a gridview on the tablets
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private ImageLoader mImageLoader;

    public GridAdapter(Context ctx) {
        mContext = ctx;
        mImageLoader = ConnectionUtil.getInstance(mContext.getApplicationContext()).getImageLoader();
    }

    @Override
    public int getCount() {
        return CacheUtil.getInstance(mContext).getProductListSize();
    }

    @Override
    public Object getItem(int position) {
        return CacheUtil.getInstance(mContext).getProductId(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * View holder class to store layout views for fast scrolling
     */
    static class ViewHolder {
        TextView productName;
        TextView price;
        NetworkImageView image;
        TextView count;
        RatingBar bar;
        TextView inStock;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, null);
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            holder.image = (NetworkImageView) convertView.findViewById(R.id.image);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.count = (TextView) convertView.findViewById(R.id.rating_count);
            holder.bar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            holder.inStock = (TextView) convertView.findViewById(R.id.inStock);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*
         * Get the product based on the current position of the gridview item
         */
        ProductDataModel product = CacheUtil.getInstance(mContext).getProduct(position);

        holder.productName.setText(StringEscapeUtils.unescapeJava(product.getProductName()));
        holder.price.setText(product.getPrice());
        holder.count.setText("("+String.valueOf(product.getReviewCount()+")"));
        holder.image.setImageUrl(product.getProductImage(), mImageLoader);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.ITEM_INDEX, position);
                mContext.startActivity(intent);
            }
        });

        holder.bar.setRating((int) product.getReviewRating());
        holder.bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if(product.isInStock()) {
            holder.inStock.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            holder.inStock.setText(R.string.in_stock_label);
        } else {
            holder.inStock.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            holder.inStock.setText(R.string.out_of_stock_label);
        }

        return convertView;
    }
}
