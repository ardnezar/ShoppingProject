package com.test.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.test.shopping.R;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.model.CacheUtil;
import com.test.shopping.model.ProductDataModel;
import com.test.shopping.view.ItemDetailActivity;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sd250307 on 3/9/16.
 */
public class ListAdapter extends BaseAdapter {
    private Context mContext;

    public ListAdapter(Context ctx) {
        mContext = ctx;
    }

    @Override
    public int getCount() {
        return CacheUtil.getInstance().getProductListSize();
    }

    @Override
    public Object getItem(int position) {
        return CacheUtil.getInstance().getProductId(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * ViewHolder static class to store the layout views for fast scrolling
     */
    static class ViewHolder {
        TextView productName;
        TextView price;
        ImageView image;
        TextView count;
        RatingBar bar;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.count = (TextView) convertView.findViewById(R.id.rating_count);
            holder.bar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String productId = CacheUtil.getInstance().getProductId(position);
        ProductDataModel product = CacheUtil.getInstance().getProduct(productId);
        holder.productName.setText(StringEscapeUtils.unescapeJava(product.getProductName()));
        holder.price.setText(product.getPrice());
        holder.count.setText("("+String.valueOf(product.getReviewCount()+")"));
        ImageLoader loader = ConnectionUtil.getInstance(mContext.getApplicationContext()).getImageLoader();
        loader.get(product.getProductImage(), ImageLoader.getImageListener(holder.image,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemDetailActivity.class);
                intent.putExtra(ItemDetailActivity.ITEM_INDEX, position);
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

        return convertView;
    }


}
