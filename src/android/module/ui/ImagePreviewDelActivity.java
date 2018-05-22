package com.giants.imagepicker.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.giants.imagepicker.ImagePicker;
import com.giants.imagepicker.util.NavigationBarChangeListener;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧），ikkong （ikkong@163.com）
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：预览已经选择的图片，并可以删除, 感谢 ikkong 的提交
 * ================================================
 */
public class ImagePreviewDelActivity extends ImagePreviewBaseActivity implements View.OnClickListener {

    private int res_btn_del;
    private int res_btn_back;
    private int res_preview_image_count;
    private int res_top_out;
    private int res_top_in;
    private int res_transparent;
    private int res_status_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context appContext = getApplicationContext();
        Resources resource = appContext.getResources();
        String pkgName = appContext.getPackageName();

        res_btn_del = resource.getIdentifier("btn_del", "id", pkgName);
        res_btn_back = resource.getIdentifier("btn_back", "id", pkgName);
        res_preview_image_count = resource.getIdentifier("preview_image_count", "string", pkgName);
        res_top_out = resource.getIdentifier("top_out", "anim", pkgName);
        res_top_in = resource.getIdentifier("top_in", "anim", pkgName);
        res_transparent = resource.getIdentifier("transparent", "color", pkgName);
        res_status_bar = resource.getIdentifier("status_bar", "color", pkgName);

        ImageView mBtnDel = (ImageView) findViewById(res_btn_del);
        mBtnDel.setOnClickListener(this);
        mBtnDel.setVisibility(View.VISIBLE);
        topBar.findViewById(res_btn_back).setOnClickListener(this);

        mTitleCount.setText(getString(res_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                mTitleCount.setText(getString(res_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
            }
        });

        NavigationBarChangeListener.with(this, NavigationBarChangeListener.ORIENTATION_HORIZONTAL)
                .setListener(new NavigationBarChangeListener.OnSoftInputStateChangeListener() {
                    @Override
                    public void onNavigationBarShow(int orientation, int height) {
                        topBar.setPadding(0, 0, height, 0);
                    }

                    @Override
                    public void onNavigationBarHide(int orientation) {
                        topBar.setPadding(0, 0, 0, 0);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == res_btn_del) {
            showDeleteDialog();
        } else if (id == res_btn_back) {
            onBackPressed();
        }
    }

    /** 是否删除此张图片 */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("要删除这张照片吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //移除当前图片刷新界面
                mImageItems.remove(mCurrentPosition);
                if (mImageItems.size() > 0) {
                    mAdapter.setData(mImageItems);
                    mAdapter.notifyDataSetChanged();
                    mTitleCount.setText(getString(res_preview_image_count, mCurrentPosition + 1, mImageItems.size()));
                } else {
                    onBackPressed();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        //带回最新数据
        intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    /** 单击时，隐藏头和尾 */
    @Override
    public void onImageSingleTap() {
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, res_top_out));
            topBar.setVisibility(View.GONE);
            tintManager.setStatusBarTintResource(res_transparent);//通知栏所需颜色
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(this, res_top_in));
            topBar.setVisibility(View.VISIBLE);
            tintManager.setStatusBarTintResource(res_status_bar);//通知栏所需颜色
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}