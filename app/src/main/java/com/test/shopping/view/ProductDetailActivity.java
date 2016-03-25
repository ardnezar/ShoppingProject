package com.test.shopping.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.test.shopping.R;
import com.test.shopping.model.CacheUtil;

public class ProductDetailActivity extends FragmentActivity {

    public static final String ITEM_INDEX = "item_index";

    private ViewPager mPager;

    private Toolbar toolbar;


    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
//        setSupportActionBar(toolbar);

        int index = getIntent().getIntExtra(ITEM_INDEX, 0);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ItemDetailPagerAdapter(this.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });
        mPager.setCurrentItem(index);
    }

    /**
     * A pager adapter creates a fragment and shows the product details.
     * To be done: Recycling the frames in the pager adapter
     */
    private class ItemDetailPagerAdapter extends FragmentStatePagerAdapter {
        public ItemDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ProductDetailFragment.create(ProductDetailActivity.this, position);
        }

        @Override
        public int getCount() {
            return CacheUtil.getInstance().getProductListSize();
        }
    }

}
