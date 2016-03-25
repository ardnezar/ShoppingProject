package com.test.shopping.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.test.shopping.R;
import com.test.shopping.model.CacheUtil;

/*
 * This is a container activity to show product details in a horizontal paged structure
 * A view pager and associated pager adaper is used to make this work.
 *
 */

public class ProductDetailActivity extends FragmentActivity {

    public static final String ITEM_INDEX = "item_index";

    private ViewPager mPager;


    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        int index = getIntent().getIntExtra(ITEM_INDEX, 0);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ItemDetailPagerAdapter(this.getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPagerAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(index);
    }

    /**
     * A pager adapter creates a fragment and shows the product details.
     * We handle the horizontal swipe through the product detailed views by implementing view pager
     * and an associated pager adapter
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
