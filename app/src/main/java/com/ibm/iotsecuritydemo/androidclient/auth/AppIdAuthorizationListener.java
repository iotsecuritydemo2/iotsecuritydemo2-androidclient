package com.ibm.iotsecuritydemo.androidclient.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.bluemix.appid.android.api.AuthorizationException;
import com.ibm.bluemix.appid.android.api.AuthorizationListener;
import com.ibm.bluemix.appid.android.api.tokens.AccessToken;
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken;
import com.ibm.iotsecuritydemo.androidclient.api.IoTDataApi;
import com.ibm.iotsecuritydemo.androidclient.core.DeviceIoTDemoApplication;
import com.ibm.iotsecuritydemo.androidclient.core.MonitoredDevicesInformation;
import com.ibm.iotsecuritydemo.androidclient.ui.activities.MonitorActivity;

import java.util.Date;

/**
 * Created by joypatra on 05/01/2018.
 */

/**
 * This listener provides the callback methods that are called at the end of App ID
 * authorization process when using the {@link com.ibm.bluemix.appid.android.api.AppID} login APIs
 */
public class AppIdAuthorizationListener implements AuthorizationListener {

    private static final String TAG = AppIdAuthorizationListener.class.getSimpleName();

    private NoticeHelper noticeHelper;
    private TokensPersistenceManager tokensPersistenceManager;
    private boolean isAnonymous;
    private Activity activity;

    public AppIdAuthorizationListener(Activity activity, AppIDAuthorizationManager authorizationManager, boolean isAnonymous) {
        tokensPersistenceManager = new TokensPersistenceManager(activity, authorizationManager);
        noticeHelper = new NoticeHelper(activity, authorizationManager, tokensPersistenceManager);
        this.isAnonymous = isAnonymous;
        this.activity = activity;
    }

    @Override
    public void onAuthorizationFailure(AuthorizationException exception) {
        Log.e(logTag("onAuthorizationFailure"),"Authorization failed", exception);
    }

    @Override
    public void onAuthorizationCanceled() {
        Log.w(logTag("onAuthorizationCanceled"),"Authorization canceled");
    }

    @Override
    public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken) {
        Log.i(logTag("onAuthorizationSuccess"),"Authorization succeeded");
        if (accessToken == null && identityToken == null) {
            Log.i(logTag("onAuthorizationSuccess"),"Both access and identity tokens are null.");

        } else {
            Log.d(TAG, "Access Token: " + (new Gson()).toJson(accessToken));
            Log.d(TAG, "Identity Token: " + (new Gson()).toJson(identityToken));

            DeviceIoTDemoApplication.get().setAnonymous(isAnonymous);

            IoTDataApi.get().getAuthorizedDevices(
                    new IoTDataApi.Listener() {
                        @Override
                        public void onSuccess(String responseText) {
                            //String[] devices = { "j.patra", "a.gantait" };
                            //Log.d(TAG, new Gson().toJson(devices));

                            DeviceIoTDemoApplication.get().setMonitoredDevicesInformation(
                                    (String[]) new Gson().fromJson(responseText, String[].class)
                            );

                            Intent intent = new Intent(activity, MonitorActivity.class);
                            intent.putExtra("auth-state", noticeHelper.determineAuthState(isAnonymous));

                            //storing the new token
                            tokensPersistenceManager.persistTokensOnDevice();

                            activity.startActivity(intent);
                            //activity.finish();
                        }

                        @Override
                        public void onFailure(String failureText) {

                        }
                    });
        }
    }

    private String logTag(String methodName){
        return this.getClass().getCanonicalName() + "." + methodName;
    }
}
