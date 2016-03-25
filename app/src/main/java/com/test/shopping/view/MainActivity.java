package com.test.shopping.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.test.shopping.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
//        setSupportActionBar(toolbar);
        Fragment newFragment = new MainFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.list_frag, newFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
