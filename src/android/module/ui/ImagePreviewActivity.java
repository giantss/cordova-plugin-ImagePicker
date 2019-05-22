package com.lzy.imagepicker.ui;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.InnerToaster;
import com.lzy.imagepicker.util.NavigationBarChangeListener;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.SuperCheckBox;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ImagePreviewActivity extends ImagePreviewBaseActivity implements ImagePicker.OnImageSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String ISORIGIN = "isOrigin";

    private boolean isOrigin;                      //是否选中原图
    private SuperCheckBox mCbCheck;                //是否选中当前图片的CheckBox
    private SuperCheckBox mCbOrigin;               //原图
    private Button mBtnOk;                         //确认图片的选择
    private View bottomBar;
    private View marginView;

    private int res_btn_ok;
    private int res_btn_back;
    private int res_cb_origin;
    private int res_ip_origin;
    private int res_ip_origin_size;
    private int res_ip_select_complete;
    private int res_ip_select_limit;
    private int res_ip_complete;
    private int res_top_out;
    private int res_top_in;
    private int res_fade_out;
    private int res_fade_in;
    private int res_ip_color_primary_dark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context appContext = getApplicationContext();
        final Resources resource = appContext.getResources();
        final String pkgName = appContext.getPackageName();

        res_btn_ok = resource.getIdentifier("btn_ok", "id", pkgName);
        res_btn_back = resource.getIdentifier("btn_back", "id", pkgName);
        res_cb_origin = resource.getIdentifier("cb_origin", "id", pkgName);
        res_ip_select_complete = resource.getIdentifier("ip_select_complete", "string", pkgName);
        res_ip_select_limit = resource.getIdentifier("ip_select_limit", "string", pkgName);
        res_ip_complete = resource.getIdentifier("ip_complete", "string", pkgName);
        res_ip_origin_size = resource.getIdentifier("ip_origin_size", "string", pkgName);
        res_ip_origin = resource.getIdentifier("ip_origin", "string", pkgName);
        res_top_out = resource.getIdentifier("top_out", "anim", pkgName);
        res_top_in = resource.getIdentifier("top_in", "anim", pkgName);
        res_fade_out = resource.getIdentifier("fade_out", "anim", pkgName);
        res_fade_in = resource.getIdentifier("fade_in", "anim", pkgName);
        res_ip_color_primary_dark = resource.getIdentifier("ip_color_primary_dark", "color", pkgName);
        final int res_ip_preview_image_count = resource.getIdentifier("ip_preview_image_count", "string", pkgName);

        isOrigin = getIntent().getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
        imagePicker.addOnImageSelectedListener(this);
        mBtnOk = (Button) findViewById(res_btn_ok);
        mBtnOk.setVisibility(View.VISIBLE);
        mBtnOk.setOnClickListener(this);

        bottomBar = findViewById(resource.getIdentifier("bottom_bar", "id", pkgName));
        bottomBar.setVisibility(View.VISIBLE);

        mCbCheck = (SuperCheckBox) findViewById(resource.getIdentifier("cb_check", "id", pkgName));
        mCbOrigin = (SuperCheckBox) findViewById(res_cb_origin);
        marginView = findViewById(resource.getIdentifier("margin_bottom", "id", pkgName));
        mCbOrigin.setText(getString(res_ip_origin));
        mCbOrigin.setOnCheckedChangeListener(this);
        mCbOrigin.setChecked(isOrigin);

        if (!imagePicker.getEnablePickOriginal()) {
            mCbOrigin.setVisibility(View.GONE);
        }

        //初始化当前页面的状态
        onImageSelected(0, null, false);
        ImageItem item = mImageItems.get(mCurrentPosition);
        boolean isSelected = imagePicker.isSelect(item);
        mTitleCount.setText(getString(res_ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
        mCbCheck.setChecked(isSelected);
        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                ImageItem item = mImageItems.get(mCurrentPosition);
                boolean isSelected = imagePicker.isSelect(item);
                mCbCheck.setChecked(isSelected);
                mTitleCount.setText(getString(res_ip_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });
        //当点击当前选中按钮的时候，需要根据当前的选中状态添加和移除图片
        mCbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItem imageItem = mImageItems.get(mCurrentPosition);
                int selectLimit = imagePicker.getSelectLimit();
                if (mCbCheck.isChecked() && selectedImages.size() >= selectLimit) {
                    InnerToaster.obj(ImagePreviewActivity.this).show(getString(res_ip_select_limit, selectLimit));
                    mCbCheck.setChecked(false);
                } else {
                    imagePicker.addSelectedImageItem(mCurrentPosition, imageItem, mCbCheck.isChecked());
                }
            }
        });
        NavigationBarChangeListener.with(this).setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
            @Override
            public void onNavigationBarShow(int orientation, int height) {
                marginView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = marginView.getLayoutParams();
                if (layoutParams.height == 0) {
                    layoutParams.height = Utils.getNavigationBarHeight(ImagePreviewActivity.this);
                    marginView.requestLayout();
                }
            }

            @Override
            public void onNavigationBarHide(int orientation) {
                marginView.setVisibility(View.GONE);
            }
        });
        NavigationBarChangeListener.with(this, NavigationBarChangeListener.ORIENTATION_HORIZONTAL)
                .setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
                    @Override
                    public void onNavigationBarShow(int orientation, int height) {
                        topBar.setPadding(0, 0, height, 0);
                        bottomBar.setPadding(0, 0, height, 0);
                    }

                    @Override
                    public void onNavigationBarHide(int orientation) {
                        topBar.setPadding(0, 0, 0, 0);
                        bottomBar.setPadding(0, 0, 0, 0);
                    }
                });
    }



    /**
     * 图片添加成功后，修改当前图片的选中数量
     * 当调用 addSelectedImageItem 或 deleteSelectedImageItem 都会触发当前回调
     */
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            mBtnOk.setText(getString(res_ip_select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
        } else {
            mBtnOk.setText(getString(res_ip_complete));
        }

        if (mCbOrigin.isChecked()) {
            long size = 0;
            for (ImageItem imageItem : selectedImages)
                size += imageItem.size;
            String fileSize = Formatter.formatFileSize(this, size);
            mCbOrigin.setText(getString(res_ip_origin_size, fileSize));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == res_btn_ok) {
            if (imagePicker.getSelectedImages().size() == 0) {
                mCbCheck.setChecked(true);
                ImageItem imageItem = mImageItems.get(mCurrentPosition);
                imagePicker.addSelectedImageItem(mCurrentPosition, imageItem, mCbCheck.isChecked());
            }
            Intent intent = new Intent();
            intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
            setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
            finish();

        } else if (id == res_btn_back) {
            Intent intent = new Intent();
            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
            setResult(ImagePicker.RESULT_CODE_BACK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == res_cb_origin) {
            if (isChecked) {
                long size = 0;
                for (ImageItem item : selectedImages)
                    size += item.size;
                String fileSize = Formatter.formatFileSize(this, size);
                isOrigin = true;
                mCbOrigin.setText(getString(res_ip_origin_size, fileSize));
            } else {
                isOrigin = false;
                mCbOrigin.setText(getString(res_ip_origin));
            }
        }
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    /**
     * 单击时，隐藏头和尾
     */
    @Override
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, res_top_out));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, res_fade_out));
            topBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(Color.TRANSPARENT);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, res_top_in));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(this, res_fade_in));
            topBar.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(res_ip_color_primary_dark);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
