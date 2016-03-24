package com.test.shopping.connectionmodule;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sdas on 8/12/15.
 */
public class ProductPageRequest extends StringRequest {

    public ProductPageRequest(String url,
                              Response.Listener<String> listener,
                              Response.ErrorListener errorListener) {

        super(Method.GET, url, listener, errorListener);
    }
}
