package com.test.shopping.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.test.shopping.R;

public class ProductListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing_container);
        Fragment newFragment = new ProductListingFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.list_frag, newFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
