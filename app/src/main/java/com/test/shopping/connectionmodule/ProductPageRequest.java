package com.test.shopping.connectionmodule;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by sujoy on 3/12/16.
 */
public class ProductPageRequest extends StringRequest {

    public ProductPageRequest(String url,
                              Response.Listener<String> listener,
                              Response.ErrorListener errorListener) {

        super(Method.GET, url, listener, errorListener);
    }
}
