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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 */
public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int  mPosition;

    private static int mTotal;

    private static Context sContext;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(Context context, int total, int position) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        mTotal = total;
        sContext = context;
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.

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

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mTotal;
    }
}
