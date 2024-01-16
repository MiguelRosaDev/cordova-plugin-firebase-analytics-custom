package by.chemerisuk.cordova.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import by.chemerisuk.cordova.support.CordovaMethod;
import by.chemerisuk.cordova.support.ReflectiveCordovaPlugin;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.EnumMap;

public class FirebaseAnalyticsPlugin extends ReflectiveCordovaPlugin {
    private static final String TAG = "FirebaseAnalyticsPlugin";

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void pluginInitialize() {
        Log.d(TAG, "Starting Firebase Analytics plugin");

        Context context = this.cordova.getActivity().getApplicationContext();

        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @CordovaMethod
    private void logEvent(String name, JSONObject params, CallbackContext callbackContext) throws JSONException {
        this.firebaseAnalytics.logEvent(name, parse(params));

        callbackContext.success();
    }

    @CordovaMethod
    private void setConsent(String name, JSONObject params, CallbackContext callbackContext) throws JSONException {
        
        Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> consentMap = new EnumMap<>(FirebaseAnalytics.ConsentType.class);
        if (params.has("ConsentTypeAnalyticsStorage") && params.getBoolean("ConsentTypeAnalyticsStorage")) {
            consentMap.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE, FirebaseAnalytics.ConsentStatus.GRANTED);
            Log.d(TAG, "batata ANALYTICS_STORAGE GRANTED");
        }else{
            consentMap.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE, FirebaseAnalytics.ConsentStatus.DENIED);
            Log.d(TAG, "batata ANALYTICS_STORAGE DENIED");
        }
        if (params.has("ConsentTypeAdStorage") && params.getBoolean("ConsentTypeAdStorage")) {
            consentMap.put(FirebaseAnalytics.ConsentType.AD_STORAGE, FirebaseAnalytics.ConsentStatus.GRANTED);
            Log.d(TAG, "batata AD_STORAGE GRANTED");
        }else{
            consentMap.put(FirebaseAnalytics.ConsentType.AD_STORAGE, FirebaseAnalytics.ConsentStatus.DENIED);
            Log.d(TAG, "batata AD_STORAGE DENIED");
        }
        if (params.has("ConsentTypeAdUserData") && params.getBoolean("ConsentTypeAdUserData")) {
            consentMap.put(FirebaseAnalytics.ConsentType.AD_USER_DATA, FirebaseAnalytics.ConsentStatus.GRANTED);
        }else{
            consentMap.put(FirebaseAnalytics.ConsentType.AD_USER_DATA, FirebaseAnalytics.ConsentStatus.DENIED);
        }
        if (params.has("ConsentTypeAdPersonalization") && params.getBoolean("ConsentTypeAdPersonalization")) {
            consentMap.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION, FirebaseAnalytics.ConsentStatus.GRANTED);
        }else{
            consentMap.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION, FirebaseAnalytics.ConsentStatus.DENIED);
        }
        
        this.firebaseAnalytics.setConsent(consentMap);

        callbackContext.success();
    }

    @CordovaMethod
    private void setUserId(String userId, CallbackContext callbackContext) {
        this.firebaseAnalytics.setUserId(userId);

        callbackContext.success();
    }

    @CordovaMethod
    private void setUserProperty(String name, String value, CallbackContext callbackContext) {
        this.firebaseAnalytics.setUserProperty(name, value);

        callbackContext.success();
    }

    @CordovaMethod
    private void resetAnalyticsData(CallbackContext callbackContext) {
        this.firebaseAnalytics.resetAnalyticsData();

        callbackContext.success();
    }

    @CordovaMethod
    private void setEnabled(boolean enabled, CallbackContext callbackContext) {
        this.firebaseAnalytics.setAnalyticsCollectionEnabled(enabled);

        callbackContext.success();
    }

    @CordovaMethod
    private void setCurrentScreen(String screenName, CallbackContext callbackContext) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        callbackContext.success();
    }

    @CordovaMethod
    private void setDefaultEventParameters(JSONObject params, CallbackContext callbackContext) throws JSONException {
        this.firebaseAnalytics.setDefaultEventParameters(parse(params));

        callbackContext.success();
    }

    @CordovaMethod
    private void requestTrackingAuthorization(JSONObject params, CallbackContext callbackContext) throws JSONException {
        //Does nothing. This is an iOS specific method.
        callbackContext.success();
    }

    private static Bundle parse(JSONObject params) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<String> it = params.keys();

        while (it.hasNext()) {
            String key = it.next();
            Object value = params.get(key);

            if (value instanceof String) {
                bundle.putString(key, (String)value);
            } else if (value instanceof Integer) {
                bundle.putInt(key, (Integer)value);
            } else if (value instanceof Double) {
                bundle.putDouble(key, (Double)value);
            } else if (value instanceof Long) {
                bundle.putLong(key, (Long)value);
            } else {
                Log.w(TAG, "Value for key " + key + " not one of (String, Integer, Double, Long)");
            }
        }

        return bundle;
    }
}
