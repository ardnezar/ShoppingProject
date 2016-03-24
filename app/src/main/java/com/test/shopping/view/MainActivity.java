package com.test.shopping.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class MainActivity extends FragmentActivity {

    private static final String TAG = "ProductMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
