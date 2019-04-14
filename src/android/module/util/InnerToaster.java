package com.lzy.imagepicker.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 外部注入toast展示，以符合app的设计风格
 */
public class InnerToaster {


    public interface IToaster {
        //提示msg
        void show(String msg);

        //提示resId资源对应的字符串
        void show(int resId);

    }

    private InnerToaster(Context aContext) {
        mContext = aContext.getApplicationContext();
    }

    private static volatile InnerToaster instance;
    private Context mContext;

    public void setIToaster(IToaster aIToaster) {
        mIToaster = aIToaster;
    }

    private IToaster mIToaster;


    public static synchronized InnerToaster obj(Context aContext) {
        if (instance == null) {
            instance = new InnerToaster(aContext);
        }
        return instance;
    }


    public void show(String msg) {
        if (mIToaster != null) {
            mIToaster.show(msg);
            return;
        }
        if (mContext == null) {
            return;
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }

    public void show(int resId) {
        if (mIToaster != null) {
            mIToaster.show(resId);
            return;
        }
        if (mContext == null) {
            return;
        }
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }
}
