package com.test.shopping.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.test.shopping.adapters.*;
//import com.test.gridproject.ListAdapter;
import com.test.shopping.R;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.connectionmodule.WebHandlerRequestCallback;
import com.test.shopping.model.CacheUtil;

import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String TAG = "ProductMain";


    private GridView mGridView;
    private GridAdapter mGridAdapter;
    private ListAdapter mListAdapter;
    private ListView mListView;
    private LinearLayout mProgressContainer;
    private LinearLayout mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        View footer = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//            .inflate(R.layout.footer, null, false);
//
//
//
//        mGridView = (GridView) findViewById(R.id.gridview);
//
//        mGridAdapter = new GridAdapter(this, IMAGE_URLS);
//
//        mGridView.setAdapter(mGridAdapter);
//
////        mGridView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
////                @Override
////                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////
////                }
////        });
            setContentView(R.layout.fragment_listview_layout);
        mProgressContainer = (LinearLayout) findViewById(R.id.progress_layout);
        mProgressContainer.setVisibility(View.VISIBLE);
        mListContainer = (LinearLayout) findViewById(R.id.list_container);
//            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//            setSupportActionBar(myToolbar);
        mListAdapter = new ListAdapter(this);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionUtil.getInstance(this).sendProductRequest(new WebHandlerRequestCallback() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
