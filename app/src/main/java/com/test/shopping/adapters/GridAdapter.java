package com.test.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.test.shopping.R;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.model.CacheUtil;
import com.test.shopping.model.ProductDataModel;
import com.test.shopping.view.ItemDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sd250307 on 3/9/16.
 */
public class GridAdapter extends BaseAdapter {

    private ArrayList<String> mUrlList;
    private Context mContext;

    public GridAdapter(Context ctx) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageV view = convertView;
//        if(view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.product_item, null);
//        }
//
//        ImageView image = (ImageView) view.findViewById(R.id.image);
//
//        ImageLoader loader = ConnectionUtil.getInstance(mContext.getApplicationContext()).getImageLoader();
//        loader.get(mUrlList.get(position), ImageLoader.getImageListener(image,
//                R.mipmap.ic_launcher, R.mipmap.ic_launcher));
//        return view;

//        Iterator<String> iter = mUrlList.iterator();
//
//        while(iter.hasNext()) {
//            iter.next();
//        }
//
//        Map<Integer, String> map = new HashMap<>();
//        Iterator <Map.Entry<Integer, String>>  it = map.entrySet().iterator();

        String productId = CacheUtil.getInstance().getProductId(position);
        ProductDataModel product = CacheUtil.getInstance().getProduct(productId);

        GridView grid = (GridView)parent;
        int size = grid.getRequestedColumnWidth();


        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        ImageLoader loader = ConnectionUtil.getInstance(mContext.getApplicationContext()).getImageLoader();
        loader.get(product.getProductImage(), ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));

//        ConnectionUtil.getInstance(mContext.getApplicationContext()).getImageLoader().

//        imageView.setImageResource(mThumbIds[position]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mUrlList.get(position)
                mContext.startActivity(new Intent(mContext, ItemDetailActivity.class));
            }
        });
        return imageView;
    }
}
