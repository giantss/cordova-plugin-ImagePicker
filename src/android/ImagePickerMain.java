package com.giants.imagepicker;


import android.Manifest;
import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ImagePickerMain extends CordovaPlugin {

    private static final String TAG = "MultiImagesPicker";

    private static CordovaWebView webView = null;
    protected static Context context = null;

    private CallbackContext callbackContext;
    private JSONObject params;

    private int maximumImagesCount;
    private int desiredWidth;
    private int desiredHeight;
    private int quality;

    public static final int PERMISSION_DENIED_ERROR = 20;
    protected final static String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
    public static final int PICK_ALBUM_SEC = 1;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        this.params = args.getJSONObject(0);

        if (action.equals("getPictures")) {

 callbackContext.success(11);

            return true;
        }

        return false;
    }


}
