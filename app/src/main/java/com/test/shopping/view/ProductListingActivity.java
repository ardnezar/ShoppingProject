package com.test.shopping.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.test.shopping.R;

public class ProductListingActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_listing_container);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        Fragment newFragment = new ProductListingFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.list_frag, newFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
