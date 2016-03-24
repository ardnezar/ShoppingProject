package com.test.shopping.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.shopping.R;
import com.test.shopping.adapters.GridAdapter;
import com.test.shopping.adapters.ListAdapter;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.connectionmodule.WebHandlerRequestCallback;

/**

 */
public class MainFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String TAG = "ShoppingMainFragment";

    private ListAdapter mListAdapter;
    private ListView mListView;
    private LinearLayout mProgressContainer;
    private LinearLayout mListContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.

//        ProductDataModel product = CacheUtil.getInstance().getProduct(CacheUtil.getInstance().getProductId(mPosition));


        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_main, container, false);


//        TextView productName = (TextView) rootView.findViewById(R.id.product_name);
//        productName.setText(StringEscapeUtils.unescapeJava(product.getProductName()));
//
//        String shortDes = product.getShortDescription();
//
//        if(shortDes != null && shortDes.length() > 0) {
//            TextView shortDesc = (TextView) rootView.findViewById(R.id.product_short_desc);
//            shortDesc.setText(Jsoup.clean(shortDes,Whitelist.simpleText()));
//        }
//
//        String longDes = product.getLongDescription();
//
//        if(longDes != null && longDes.length() > 0) {
//            TextView longDesc = (TextView) rootView.findViewById(R.id.long_description);
//            longDesc.setText(Jsoup.clean(longDes, Whitelist.simpleText()));
//        }
//
//        TextView price = (TextView) rootView.findViewById(R.id.price);
//        price.setText(product.getPrice());
//
//        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
//        ImageLoader loader = ConnectionUtil.getInstance(sContext).getImageLoader();
//        loader.get(product.getProductImage(), ImageLoader.getImageListener(imageView,
//                R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        mProgressContainer = (LinearLayout) rootView.findViewById(R.id.progress_layout);
        mProgressContainer.setVisibility(View.VISIBLE);
        mListContainer = (LinearLayout) rootView.findViewById(R.id.list_container);
//            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//            setSupportActionBar(myToolbar);
        mListAdapter = new ListAdapter(getActivity());

        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setAdapter(mListAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionUtil.getInstance(getContext()).sendProductRequest(new WebHandlerRequestCallback() {
            @Override
            public void updateData() {
                Log.d(TAG, "updateData");
                mListAdapter.notifyDataSetChanged();
                mProgressContainer.setVisibility(View.GONE);
                mListContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void updateError() {
                mProgressContainer.setVisibility(View.GONE);
                mListContainer.setVisibility(View.VISIBLE);
            }
        });
    }
}
