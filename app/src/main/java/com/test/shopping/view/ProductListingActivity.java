package com.test.shopping.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.test.shopping.BuildConfig;
import com.test.shopping.R;
import com.test.shopping.model.CacheUtil;

public class ProductListingActivity extends AppCompatActivity {
    private static final String TAG = "ShoppingListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing_container);
        Fragment newFragment = new ProductListingFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.list_frag, newFragment).commit();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //Cleanup caches if the current process is in the middle of background LRU
        if(BuildConfig.DEBUG)Log.d(TAG, "onTrimMemory level:"+level);
        if(level >= TRIM_MEMORY_COMPLETE) {
            CacheUtil.getInstance(this).cleanup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
