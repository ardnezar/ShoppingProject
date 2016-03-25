package com.test.shopping.connectionmodule;

/**
 * Created by sujoy on 3/23/16.
 * This callback interface is used by the connection module to update the View elements once the
 * network request is completed.
 */
public interface WebHandlerRequestCallback {
    /*
     * This callback method is used to update the view when API request is successful.
     */
    void updateData();

    /*
     * This callback method is used to update the view when API request has resulted in a failure.
     */
    void updateError();
}

