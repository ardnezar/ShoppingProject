package com.test.shopping.view;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.test.shopping.BuildConfig;
import com.test.shopping.R;
import com.test.shopping.adapters.GridAdapter;
import com.test.shopping.adapters.ListAdapter;
import com.test.shopping.connectionmodule.ConnectionUtil;
import com.test.shopping.connectionmodule.WebHandlerRequestCallback;
import com.test.shopping.model.CacheUtil;

/**
 * This Fragment is the container of the list view of the products on the device
 * This supports listing the products on both the phone and the tablet based screen size
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
    private Spinner mSortSpinner;

    private static final int MORE_DATA_REQUEST = 1;
    // We delay the data fetch for 1 second. In case user decides to scroll back and not interested
    // in new content
    private static final int DATA_REQUEST_DELAY = 1 * 1000;

    private int mSortType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.product_list_fragment, container, false);

        mProgressContainer = (LinearLayout) rootView.findViewById(R.id.progress_layout);
        mProgressContainer.setVisibility(View.VISIBLE);
        mListContainer = (LinearLayout) rootView.findViewById(R.id.list_container);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mSortSpinner = (Spinner) rootView.findViewById(R.id.sort_type);

        final SharedPreferences pref = getActivity().getSharedPreferences(CacheUtil.PREF_FILE, Context.MODE_PRIVATE);


        mSortType = pref.getInt(CacheUtil.SORT_TYPE_KEY, CacheUtil.SORT_NAME);



        if(mListView != null) {
            // This is Phone device.
            mListAdapter = new ListAdapter(getActivity());
            mListView.setAdapter(mListAdapter);
            mFooterView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                    inflate(R.layout.lazy_loading_footer, null, false);

            mListView.setOnScrollListener(mScrollListener);
        } else {
            // If listview is not valid, this is a tablet. Configure Gridview and associate gridview
            // adapter
            mGridView = (GridView) rootView.findViewById(R.id.gridview);
            if (mGridView != null) {
                mGridAdapter = new GridAdapter(getActivity());
                mGridView.setAdapter(mGridAdapter);
                mGridView.setOnScrollListener(mScrollListener);
            }
        }

        mSortSpinner.post(new Runnable() {
            @Override
            public void run() {
                mSortSpinner.setSelection(mSortType);
            }
        });

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                mSortType = pos;
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(CacheUtil.SORT_TYPE_KEY, pos);
                editor.commit();
                CacheUtil.getInstance(getActivity()).updateProductIdList();
                mHandler.sendEmptyMessageDelayed(UPDATE_SORT, UPDATE_SORT_DELAY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    /*
     * Implement the scroll listener. This has all the lazy loading code implementation.
     */
    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {

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
            if (mCurrentVisibleItemCount > 0 &&
                    mCurrentScrollState == SCROLL_STATE_IDLE &&
                    mTotalItemCount == (mCurrentFirstVisibleItem + mCurrentVisibleItemCount)) {
                    /*
                     * If the scroll is completed and there is not current loading happening,
                     * load more content if present
                     */
                if (!mLoadingMore) {
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
                mLoadingMore = false;
                mHandler.removeMessages(MORE_DATA_REQUEST);
            }
        }
    };

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
               if(inputMessage.what == MORE_DATA_REQUEST) {
                   if(BuildConfig.DEBUG)Log.d(TAG, "handleMessage..calling sendProductRequest");
                   sendProductRequest();
               } else {
                   if(BuildConfig.DEBUG)Log.d(TAG, "List updated on sorting");
                   if(mListAdapter != null) {
                       mListAdapter.notifyDataSetChanged();
                   } else if(mGridAdapter != null) {
                       mGridAdapter.notifyDataSetChanged();
                   }
               }
        }
    };

    private static final int UPDATE_SORT = 2001;
    private static final int UPDATE_SORT_DELAY = 1000;

    @Override
    public void onResume() {
        super.onResume();
        if((mListAdapter != null && mListAdapter.getCount() <= 0) ||
                (mGridAdapter != null && mGridAdapter.getCount() <= 0)) {
            if(BuildConfig.DEBUG)Log.d(TAG, "sending product request..");
            sendProductRequest();
        }
    }

    //This method will send the product content request to connection module
    private void sendProductRequest() {
        ConnectionUtil.getInstance(getActivity()).sendProductRequest(new WebHandlerRequestCallback() {
            @Override
            public void updateData() {
                /*
                 * Once the content is updated we get this callback and we update the product lists
                 * accordingly.
                 */
                CacheUtil.getInstance(getActivity()).updateProductIdList();

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
                // In case the progress dialog is being shown, remove it after getting the content
                // and make the list content visible
                mProgressContainer.setVisibility(View.GONE);
                mListContainer.setVisibility(View.VISIBLE);
                if(mListView!= null) {
                    //Footer for lazy loading is only applicable for listview on phone for now
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
}
