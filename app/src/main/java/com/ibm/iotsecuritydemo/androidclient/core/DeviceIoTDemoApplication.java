package com.ibm.iotsecuritydemo.androidclient.core;

import android.app.Application;
import android.content.Context;

import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;

import java.text.SimpleDateFormat;

/**
 * Created by joypatra on 05/01/2018.
 */

public class DeviceIoTDemoApplication extends Application {
    private static final String TAG = DeviceIoTDemoApplication.class.getSimpleName();

    private static DeviceIoTDemoApplication ourInstance = new DeviceIoTDemoApplication();

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Context launcherActivity;

    // Configuration for old BMS OAuth service - no longer required
    //public static final String APPLICATION_ROUTE = "http://iotsecuritydemo.mybluemix.net";
    //public static final String APPLICATION_ID = "your_application_id";
    //public static final String APPLICATION_OAUTH_REALM = "iotsecuritydemoRealm";

    // Dec-2017: Configuration for new IBM Cloud AppID service
    public static final String IBMCLOUDAPPID_TENANT_ID = "12bd34fd-5db0-4506-your-tenantid";
    public static final String IBMCLOUDAPPID_REGION_ID = AppID.REGION_US_SOUTH;
    public static final String BMSCLIENT_REGION_ID = BMSClient.REGION_US_SOUTH;


    public static final int DEVICE_MONITOR_TIMER_INITIAL = 2000; // millisecs
    public static final int DEVICE_MONITOR_TIMER_INTERVAL = 5000; // millisecs

    // The devices that the logged in user is allowed to monitor, as per the list
    // of devices provided by the server-side custom API
    private MonitoredDevicesInformation monitoredDevicesInformation = new MonitoredDevicesInformation();

    private boolean isAnonymous = false;

    /**
     * A dummy response mimicking /iotf/devices API response from the server.
     * This is used for dummy test of the UI.
     */
    public static final String DUMMY_DEVICE_LIST = "[\"j.patra\", \"a.gantait\" ]";

    public static final String TOPIC_FORMAT = "iot-2/type/{0}/id/{1}/mon";

    /**
     * If a dummy login was used, then this single event is treated as a
     * dummy device data.
     *
     * This is also useful to understand how a single event data looks like when
     * retrieved from the demo server.
     */
    public static final String DUMMY_DEVICE_DATA = "{\n" +
            "   \"docs\":[\n" +
            "      {\n" +
            "         \"_id\":\"4cf3d05b9c33cd05159d1f688727b4a4\",\n" +
            "         \"_rev\":\"1-34431ee058f9ae0f749dcd481af372b8\",\n" +
            "         \"recordType\":\"event\",\n" +
            "         \"deviceType\":\"MQTTDevice\",\n" +
            "         \"deviceId\":\"j.patra\",\n" +
            "         \"eventType\":\"iotevt\",\n" +
            "         \"format\":\"json\",\n" +
            "         \"timestamp\":\"1455909252026\",\n" +
            "         \"payload\":{\n" +
            "            \"d\":{\n" +
            "               \"timestampMillis\":1455909252026,\n" +
            "               \"ACCELEROMETER_0\":-7.814,\n" +
            "               \"ACCELEROMETER_1\":1.992,\n" +
            "               \"ACCELEROMETER_2\":9.194,\n" +
            "               \"PROXIMITY_0\":1,\n" +
            "               \"PROXIMITY_1\":0,\n" +
            "               \"PROXIMITY_2\":0,\n" +
            "               \"LIGHT_0\":6,\n" +
            "               \"LIGHT_1\":0,\n" +
            "               \"LIGHT_2\":0,\n" +
            "               \"MAGNETOMETER_0\":-1.812,\n" +
            "               \"MAGNETOMETER_1\":31.938,\n" +
            "               \"MAGNETOMETER_2\":-22.812,\n" +
            "               \"ORIENTATION_0\":20.156,\n" +
            "               \"ORIENTATION_1\":-9.359,\n" +
            "               \"ORIENTATION_2\":-39.734,\n" +
            "               \"AKM Software Virtual Gyroscope sensor _0\":0.722,\n" +
            "               \"AKM Software Virtual Gyroscope sensor _1\":1.297,\n" +
            "               \"AKM Software Virtual Gyroscope sensor _2\":-0.051,\n" +
            "               \"AKM Rotation vector sensor_0\":0.009,\n" +
            "               \"AKM Rotation vector sensor_1\":0.256,\n" +
            "               \"AKM Rotation vector sensor_2\":-0.187,\n" +
            "               \"AKM Rotation vector sensor_3\":0,\n" +
            "               \"AKM Rotation vector sensor_4\":-1,\n" +
            "               \"AKM Gravity sensor_0\":-4.794,\n" +
            "               \"AKM Gravity sensor_1\":-0.763,\n" +
            "               \"AKM Gravity sensor_2\":8.526,\n" +
            "               \"AKM Linear acceleration sensor_0\":-3.024,\n" +
            "               \"AKM Linear acceleration sensor_1\":2.751,\n" +
            "               \"AKM Linear acceleration sensor_2\":0.667,\n" +
            "               \"LOCATION_0\":22.672,\n" +
            "               \"LOCATION_1\":88.442\n" +
            "            }\n" +
            "         }\n" +
            "      }\n" +
            "   ]\n" +
            "}";


    public static DeviceIoTDemoApplication get() {
        return ourInstance;
    }

    private DeviceIoTDemoApplication() {
    }

    public DeviceIoTDemoApplication reset() {
        launcherActivity = null;
        return this;
    }

    public DeviceIoTDemoApplication initialize(Context context) {
        this.launcherActivity = context;
        return this;
    }

    public MonitoredDevicesInformation getMonitoredDevicesInformation() {
        return monitoredDevicesInformation;
    }

    public void setMonitoredDevicesInformation(String[] devices) {
        this.monitoredDevicesInformation.devices = devices;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
