package com.ibm.iotsecuritydemo.androidclient.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.bluemix.appid.android.api.LoginWidget;
import com.ibm.iotsecuritydemo.androidclient.R;
import com.ibm.iotsecuritydemo.androidclient.auth.AppIdAuthorizationListener;
import com.ibm.iotsecuritydemo.androidclient.auth.TokensPersistenceManager;
import com.ibm.iotsecuritydemo.androidclient.core.DeviceIoTDemoApplication;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;

    private AppID appId;
    private BMSClient bmsClient;
    private AppIDAuthorizationManager appIDAuthorizationManager;
    private TokensPersistenceManager tokensPersistenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_login_anon).setOnClickListener(this);

        // Initialize IBM Cloud AppID service SDK with tenant ID and region of the AppID service to connect to
        // You can find your tenant ID and region in the Service Credentials section at left hand side of your AppID service dashboard
        appId = AppID.getInstance();

        appId.initialize(
                getApplicationContext(),
                DeviceIoTDemoApplication.IBMCLOUDAPPID_TENANT_ID,
                DeviceIoTDemoApplication.IBMCLOUDAPPID_REGION_ID);

        bmsClient = BMSClient.getInstance();

        bmsClient.initialize(
                getApplicationContext(),
                DeviceIoTDemoApplication.BMSCLIENT_REGION_ID
        );

        appIDAuthorizationManager = new AppIDAuthorizationManager(this.appId);
        tokensPersistenceManager = new TokensPersistenceManager(this, appIDAuthorizationManager);

        bmsClient.setAuthorizationManager(appIDAuthorizationManager);

        Log.d(TAG, "IBM Cloud AppID client initialized.");

        Logger.setLogLevel(Logger.LEVEL.DEBUG);
        Logger.setSDKDebugLoggingEnabled(true);
    }


    public void startLoginActivity(View view) {

        LoginWidget loginWidget = appId.getLoginWidget();
        // Using persisted access token is optional
        final String storedAccessToken = tokensPersistenceManager.getStoredAccessToken();

        AppIdAuthorizationListener appIdAuthorizationListener =
                new AppIdAuthorizationListener(
                        this,
                        appIDAuthorizationManager,
                        false);

        loginWidget.launch(this, appIdAuthorizationListener);
    }

    public void startLoginAnonymousActivity(View view) {

        Log.i(TAG,"Attempting anonymous authorization");

        final String storedAccessToken = tokensPersistenceManager.getStoredAnonymousAccessToken();
        AppIdAuthorizationListener appIdAuthorizationListener =
                new AppIdAuthorizationListener(
                        this,
                        appIDAuthorizationManager,
                        true);

        appId.loginAnonymously(getApplicationContext(), appIdAuthorizationListener);
    }

    @Override
    public void onClick(View view) {

        showProgress();

        if (view.getId() == R.id.btn_login) {
            startLoginActivity(view);
        } else if (view.getId() == R.id.btn_login_anon) {
            startLoginAnonymousActivity(view);
        }
    }

    private void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.bringToFront();
            }
        });
    }
}
