package com.message.tasha.util;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Puru Chauhan on 22/05/16.
 */
public class CheckGServices {

    private static final String TAG = "BaseLocationActivity";

    private AppCompatActivity context;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public CheckGServices(AppCompatActivity context){
        this.context=context;
    }

    // Play services check method
    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();

            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

}
