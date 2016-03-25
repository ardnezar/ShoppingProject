/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.shopping.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.R;
import com.test.shopping.model.CacheUtil;
import com.test.shopping.model.ProductDataModel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * A fragment representing a single page in the view pager showing details of each item.
 *
 */
public class ItemDetailFragment extends Fragment {

    public static final String ARG_PAGE = "page";

    private int  mPosition;

    private static Context sContext;

    public static ItemDetailFragment create(Context context, int position) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        sContext = context;
        return fragment;
    }

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        /*
         * Inflate the layout based on the current product details
         */

        ProductDataModel product = CacheUtil.getInstance().getProduct(CacheUtil.getInstance().getProductId(mPosition));


        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.product_item, container, false);


        TextView productName = (TextView) rootView.findViewById(R.id.product_name);
        productName.setText(StringEscapeUtils.unescapeJava(product.getProductName()));

        String shortDes = product.getShortDescription();

        if(shortDes != null && shortDes.length() > 0) {
            TextView shortDesc = (TextView) rootView.findViewById(R.id.product_short_desc);
            shortDesc.setText(Jsoup.clean(shortDes,Whitelist.simpleText()));
        }

        String longDes = product.getLongDescription();

        if(longDes != null && longDes.length() > 0) {
            TextView longDesc = (TextView) rootView.findViewById(R.id.long_description);
            longDesc.setText(Jsoup.clean(longDes, Whitelist.simpleText()));
        }

        TextView price = (TextView) rootView.findViewById(R.id.price);
        price.setText(product.getPrice());

        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        ImageLoader loader = ConnectionUtil.getInstance(sContext).getImageLoader();
        loader.get(product.getProductImage(), ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        TextView ratingCountView = (TextView) rootView.findViewById(R.id.rating_count);

        ratingCountView.setText("("+String.valueOf(product.getReviewCount()+")"));

        RatingBar bar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        bar.setRating((int) product.getReviewRating());
        bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }
}
