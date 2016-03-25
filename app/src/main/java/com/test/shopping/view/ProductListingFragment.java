package com.test.shopping.view;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.test.shopping.BuildConfig;
import com.test.shopping.R;
import com.test.shopping.adapters.GridAdapter;
import com.test.shopping.adapters.ListAdapter;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.connectionmodule.WebHandlerRequestCallback;

/**
 * This Fragment is the container of the listview of the products in the Phone device
 */
public class ProductListingFragment extends Fragment {

    public static final String TAG = "ShoppingMainFragment";

    private ListAdapter mListAdapter;
    private ListView mListView;
    private GridAdapter mGridAdapter;
    private GridView mGridView;
    private LinearLayout mProgressContainer;
    private LinearLayout mListContainer;
    private View mFooterView;
    private int mCurrentFirstVisibleItem = 0;
    private int mCurrentVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private int mCurrentScrollState = 0;
    private boolean mLoadingMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.product_list_fragment, container, false);

        mProgressContainer = (LinearLayout) rootView.findViewById(R.id.progress_layout);
        mProgressContainer.setVisibility(View.VISIBLE);
        mListContainer = (LinearLayout) rootView.findViewById(R.id.list_container);
        mListView = (ListView) rootView.findViewById(R.id.listView);

        if(mListView != null) {
            mListAdapter = new ListAdapter(getActivity());
            mListView.setAdapter(mListAdapter);
            mFooterView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.lazy_loading_footer, null, false);

            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {
                    mCurrentFirstVisibleItem = firstVisibleItem;
                    mCurrentVisibleItemCount = visibleItemCount;
                    mTotalItemCount = totalItemCount;
                }

                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    mCurrentScrollState = scrollState;
                    isScrollCompleted();
                }

                private void isScrollCompleted() {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "isScrollCompleted...mCurrentVisibleItemCount:" + mCurrentVisibleItemCount + "," +
                                "mCurrentFirstVisibleItem:" + mCurrentFirstVisibleItem +
                                ",mCurrentScrollState:" + mCurrentScrollState + ",mTotalItemCount:" + mTotalItemCount);
                    if (mCurrentVisibleItemCount > 0 &&
                            mCurrentScrollState == SCROLL_STATE_IDLE &&
                            mTotalItemCount == (mCurrentFirstVisibleItem + mCurrentVisibleItemCount)) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "isScrollCompleted..1");
                    /*
                     * If the scroll is completed and there is not current loading happening,
                     * load more content if present
                     */
                        if (!mLoadingMore) {
                            if (BuildConfig.DEBUG) Log.d(TAG, "isScrollCompleted..2");
                            mLoadingMore = true;

                        /*
                         * Send a delayed message to fetch content after showing the footer
                         * That way if user scrolls in the top direction,we don't have to request
                         * the next set of content based on the user intent to look into already
                         * listed items in the listview
                         */

                            mHandler.sendEmptyMessageDelayed(MORE_DATA_REQUEST, DATA_REQUEST_DELAY);
                        }
                    } else if (mCurrentScrollState != SCROLL_STATE_IDLE &&
                            mTotalItemCount != (mCurrentFirstVisibleItem + mCurrentVisibleItemCount + 1)) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "isScrollCompleted..3");
                        mLoadingMore = false;
//                        if (mListView != null) {
//                            mListView.removeFooterView(mFooterView);
//                        }
                        mHandler.removeMessages(MORE_DATA_REQUEST);
                    }
                }
            });
        }

        mGridView = (GridView) rootView.findViewById(R.id.gridview);

        if(mGridView != null) {
            mGridAdapter = new GridAdapter(getActivity());
            mGridView.setAdapter(mGridAdapter);
            mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {
                    mCurrentFirstVisibleItem = firstVisibleItem;
                    mCurrentVisibleItemCount = visibleItemCount;
                    mTotalItemCount = totalItemCount;
                }

                @Override
                public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                    mCurrentScrollState = scrollState;
                    isScrollCompleted();
                }

                private void isScrollCompleted() {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "isScrollCompleted...mCurrentVisibleItemCount:" + mCurrentVisibleItemCount + "," +
                                "mCurrentFirstVisibleItem:" + mCurrentFirstVisibleItem +
                                ",mCurrentScrollState:" + mCurrentScrollState + ",mTotalItemCount:" + mTotalItemCount);
                    if (mCurrentVisibleItemCount > 0 &&
                            mCurrentScrollState == SCROLL_STATE_IDLE &&
                            mTotalItemCount == (mCurrentFirstVisibleItem + mCurrentVisibleItemCount)) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "isScrollCompleted..1");
                    /*
                     * If the scroll is completed and there is not current loading happening,
                     * load more content if present
                     */
                        if (!mLoadingMore) {
                            if (BuildConfig.DEBUG) Log.d(TAG, "isScrollCompleted..2");
                            mLoadingMore = true;

                        /*
                         * Send a delayed message to fetch content after showing the footer
                         * That way if user scrolls in the top direction,we don't have to request
                         * the next set of content based on the user intent to look into already
                         * listed items in the listview
                         */

                            mHandler.sendEmptyMessageDelayed(MORE_DATA_REQUEST, DATA_REQUEST_DELAY);
                        }
                    } else if (mCurrentScrollState != SCROLL_STATE_IDLE &&
                            mTotalItemCount != (mCurrentFirstVisibleItem + mCurrentVisibleItemCount + 1)) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "isScrollCompleted..3");
                        mLoadingMore = false;
                        mHandler.removeMessages(MORE_DATA_REQUEST);
                    }
                }
            });
        }

        return rootView;
    }

    private static final int MORE_DATA_REQUEST = 1;
    private static final int DATA_REQUEST_DELAY = 5 * 1000;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
               if(inputMessage.what == MORE_DATA_REQUEST) {
                   if(BuildConfig.DEBUG)Log.d(TAG, "handleMessage..calling sendProductRequest");
                   sendProductRequest();
               }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(!mConfigChange || mListAdapter.getCount() <= 0) {
            if(BuildConfig.DEBUG)Log.d(TAG, "sending product request");
            sendProductRequest();
        }
    }

    private boolean mConfigChange = false;

    private void sendProductRequest() {
        ConnectionUtil.getInstance(getActivity()).sendProductRequest(new WebHandlerRequestCallback() {
            @Override
            public void updateData() {
                if(BuildConfig.DEBUG)Log.d(TAG, "updateData");
                mLoadingMore = false;
                if(mListView != null) {
                    mListView.removeFooterView(mFooterView);
                }
                if(mListAdapter != null) {
                    mListAdapter.notifyDataSetChanged();
                }
                if(mGridAdapter != null) {
                    mGridAdapter.notifyDataSetChanged();
                }
                mProgressContainer.setVisibility(View.GONE);
                mListContainer.setVisibility(View.VISIBLE);
                if(mListView!= null) {
                    mListView.addFooterView(mFooterView);
                }

            }

            @Override
            public void updateError() {
                mProgressContainer.setVisibility(View.GONE);
                mListContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mConfigChange = true;
    }
}
