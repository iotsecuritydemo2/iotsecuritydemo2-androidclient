package com.ibm.iotsecuritydemo.androidclient.api;

import android.util.Log;

import com.ibm.iotsecuritydemo.androidclient.core.DeviceIoTDemoApplication;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Request;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by joypatra on 05/02/2018.
 */

public class IoTDataApi {

    private static IoTDataApi instance = new IoTDataApi();

    public static IoTDataApi get() { return instance; }

    public interface Listener {
        public void onSuccess(String responseText);
        public void onFailure(String failureText);
    }

    private final static String TAG = IoTDataApi.class.getSimpleName();

    private final static String SERVER_HOSTNAME = "https://iotsecuritydemo2.mybluemix.net";

    private class CustomResponseListener implements ResponseListener {
        private Listener listener;

        public CustomResponseListener(Listener listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(Response response) {
            Log.d(TAG,
                    "onSuccess(): \n" +
                            DeviceIoTDemoApplication.DATE_FORMAT.format(new Date()) + ": \n" +
                            response.getResponseText());

            listener.onSuccess(response.getResponseText());
        }

        @Override
        public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
            final String errorMessage =
                    (null != response ? response.getResponseText() : "") + "\n"
                            + (null != t ? t.toString() : "") + "\n"
                            + (null != extendedInfo ? extendedInfo.toString() : "") + "\n";

            Log.d(TAG,
                    "onFailure(): \n" +
                            DeviceIoTDemoApplication.DATE_FORMAT.format(new Date()) + ": \n" +
                            errorMessage);

            listener.onFailure(errorMessage);
        }
    }

    public void getAuthorizedDevices(final Listener listener) {

        if (DeviceIoTDemoApplication.get().isAnonymous()) {
            listener.onSuccess(DeviceIoTDemoApplication.DUMMY_DEVICE_LIST);
        } else {
            request(SERVER_HOSTNAME + "/iotf/devices/", listener);
        }
    }

    public void getDeviceData(String deviceId, final Listener listener) {
        if (DeviceIoTDemoApplication.get().isAnonymous()) {
            listener.onSuccess(DeviceIoTDemoApplication.DUMMY_DEVICE_DATA);
        } else {
            // We are requesting only the last event, hence count=1
            request(SERVER_HOSTNAME + "/iotf/devices/" + deviceId + "?count=1", listener);
        }
    }

    private void request(String url, final Listener listener) {
        Log.i(TAG, "Invoking: "+url);
        new Request(url, Request.GET).send(DeviceIoTDemoApplication.get(), new CustomResponseListener(listener));
    }
}
