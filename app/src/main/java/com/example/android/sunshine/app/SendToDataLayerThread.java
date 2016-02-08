package com.example.android.sunshine.app;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by harry on 8/02/2016.
 */
public class SendToDataLayerThread extends Thread {

    private GoogleApiClient googleClient;
    private PutDataMapRequest request;

    public SendToDataLayerThread(GoogleApiClient googleClient, PutDataMapRequest request) {
        this.googleClient = googleClient;
        this.request = request;
    }

    public void run() {
        Wearable.DataApi.putDataItem(googleClient, request.asPutDataRequest())
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (dataItemResult.getStatus().isSuccess()) {
                            Log.e("myTag", "Message" + request.getDataMap().toString());
                        }
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.e("myTag", "buildWatchOnlyNotification(): Failed to set the data, "
                                    + "status: " + dataItemResult.getStatus().getStatusCode());
                        }
                        googleClient.disconnect();
                    }
                });
    }

}
