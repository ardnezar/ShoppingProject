package com.test.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        }

        String productId = CacheUtil.getInstance().getProductId(position);
        ProductDataModel product = CacheUtil.getInstance().getProduct(productId);

        TextView productName = (TextView) convertView.findViewById(R.id.product_name);
        productName.setText(StringEscapeUtils.unescapeJava(product.getProductName()));

        TextView price = (TextView) convertView.findViewById(R.id.price);
        price.setText(product.getPrice());

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        ImageLoader loader = ConnectionUtil.getInstance(mContext.getApplicationContext()).getImageLoader();
        loader.get(product.getProductImage(), ImageLoader.getImageListener(image,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemDetailActivity.class);
                intent.putExtra(ItemDetailActivity.ITEM_INDEX, position);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
