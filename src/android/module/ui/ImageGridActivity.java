package com.lzy.imagepicker.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lzy.imagepicker.DataHolder;
import com.lzy.imagepicker.ImageDataSource;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.adapter.ImageFolderAdapter;
import com.lzy.imagepicker.adapter.ImageRecyclerAdapter;
import com.lzy.imagepicker.adapter.ImageRecyclerAdapter.OnImageItemClickListener;
import com.lzy.imagepicker.bean.ImageFolder;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.FolderPopUpWindow;
import com.lzy.imagepicker.view.SuperCheckBox;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * 2017-03-17
 *
 * @author nanchen
 * 新增可直接传递是否裁剪参数，以及直接拍照
 * ================================================
 */
public class ImageGridActivity extends ImageBaseActivity implements ImageDataSource.OnImagesLoadedListener, OnImageItemClickListener, ImagePicker.OnImageSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;
    public static final String EXTRAS_TAKE_PICKERS = "TAKE";
    public static final String EXTRAS_IMAGES = "IMAGES";

    private ImagePicker imagePicker;

    private int res_btn_ok;
    private int res_btn_back;
    private int res_ll_dir;
    private int res_btn_preview;
    private int res_cb_origin;
    private int res_ip_origin;
    private int res_ip_origin_size;
    private int res_ip_select_complete;
    private int res_ip_complete;
    private int res_ip_preview;
    private int res_ip_preview_count;
    private int res_ip_str_no_permission;
    private int res_ip_str_no_camera_permission;
    private int res_ip_text_primary_inverted;
    private int res_ip_text_secondary_inverted;
    private boolean isOrigin = false;  //是否选中原图
    private View mFooterBar;     //底部栏
    private Button mBtnOk;       //确定按钮
    private View mllDir; //文件夹切换按钮
    private TextView mtvDir; //显示当前文件夹
    private TextView mBtnPre;      //预览按钮
    private SuperCheckBox mCbOrigin;               //原图
    private ImageFolderAdapter mImageFolderAdapter;    //图片文件夹的适配器
    private FolderPopUpWindow mFolderPopupWindow;  //ImageSet的PopupWindow
    private List<ImageFolder> mImageFolders;   //所有的图片文件夹
    //    private ImageGridAdapter mImageGridAdapter;  //图片九宫格展示的适配器
    private boolean directPhoto = false; // 默认不是直接调取相机
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        directPhoto = savedInstanceState.getBoolean(EXTRAS_TAKE_PICKERS, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRAS_TAKE_PICKERS, directPhoto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context appContext = getApplicationContext();
        Resources resource = appContext.getResources();
        String pkgName = appContext.getPackageName();

        res_btn_ok = resource.getIdentifier("btn_ok", "id", pkgName);
        res_btn_back = resource.getIdentifier("btn_back", "id", pkgName);
		res_ll_dir = resource.getIdentifier("ll_dir", "id", pkgName);
        res_btn_preview = resource.getIdentifier("btn_preview", "id", pkgName);
        res_cb_origin = resource.getIdentifier("cb_origin", "id", pkgName);
        res_ip_origin_size = resource.getIdentifier("ip_origin_size", "string", pkgName);
        res_ip_origin = resource.getIdentifier("ip_origin", "string", pkgName);
        res_ip_select_complete = resource.getIdentifier("ip_select_complete", "string", pkgName);
        res_ip_complete = resource.getIdentifier("ip_complete", "string", pkgName);
        res_ip_preview = resource.getIdentifier("ip_preview", "string", pkgName);
        res_ip_preview_count = resource.getIdentifier("ip_preview_count", "string", pkgName);
        res_ip_str_no_permission = resource.getIdentifier("ip_str_no_permission", "string", pkgName);
        res_ip_str_no_camera_permission = resource.getIdentifier("ip_str_no_camera_permission", "string", pkgName);
        res_ip_text_primary_inverted = resource.getIdentifier("ip_text_primary_inverted", "color", pkgName);
        res_ip_text_secondary_inverted = resource.getIdentifier("ip_text_secondary_inverted", "color", pkgName);
		setContentView(resource.getIdentifier("activity_image_grid", "layout", pkgName));

        imagePicker = ImagePicker.getInstance();
        imagePicker.clear();
        imagePicker.addOnImageSelectedListener(this);
        if (imagePicker.getSelectLimit() == 0 || imagePicker.getSelectLimit() == 1) {
            imagePicker.setSelectLimit(1);
            imagePicker.setMultiMode(false);
        } else {
            imagePicker.setMultiMode(true);
        }

        Intent data = getIntent();
        // 新增可直接拍照
        if (data != null && data.getExtras() != null) {
            directPhoto = data.getBooleanExtra(EXTRAS_TAKE_PICKERS, false); // 默认不是直接打开相机
            if (directPhoto) {
                checkToCapture();
            }
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(EXTRAS_IMAGES);
            imagePicker.setSelectedImages(images);
        }

        mRecyclerView = (RecyclerView) findViewById(resource.getIdentifier("recycler", "id", pkgName));

        findViewById(res_btn_back).setOnClickListener(this);
        mBtnOk = (Button) findViewById(res_btn_ok);
        mBtnOk.setOnClickListener(this);
        mBtnPre = (TextView) findViewById(res_btn_preview);
        mBtnPre.setOnClickListener(this);
        mFooterBar = findViewById(resource.getIdentifier("footer_bar", "id", pkgName));
        mllDir = findViewById(res_ll_dir);
        mllDir.setOnClickListener(this);
        mtvDir = (TextView) findViewById(resource.getIdentifier("tv_dir", "id", pkgName));
        if (imagePicker.isMultiMode()) {
            mBtnOk.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.VISIBLE);
        } else {
            mBtnOk.setVisibility(View.GONE);
            mBtnPre.setVisibility(View.GONE);
        }

        mCbOrigin = (SuperCheckBox) findViewById(res_cb_origin);
        mCbOrigin.setText(getString(res_ip_origin));
        mCbOrigin.setOnCheckedChangeListener(this);
        mCbOrigin.setChecked(isOrigin);

        if (!imagePicker.getEnablePickOriginal()) {
            mCbOrigin.setVisibility(View.GONE);
        }

        // mImageGridAdapter = new ImageGridAdapter(this, null);
        mImageFolderAdapter = new ImageFolderAdapter(this, null);
        mRecyclerAdapter = new ImageRecyclerAdapter(this, null);

        onImageSelected(0, null, false);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new ImageDataSource(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            new ImageDataSource(this, null, this);
        }

    }

    private void checkToCapture() {
        if (!(checkPermission(Manifest.permission.CAMERA)) || !checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, ImageGridActivity.REQUEST_PERMISSION_CAMERA);
        } else {
            imagePicker.takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ImageDataSource(this, null, this);
            } else {
                showToast(getString(res_ip_str_no_permission));
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            boolean denied = false;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    denied = true;
                }
            }
            if (!denied) {
                imagePicker.takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
            } else {
                showToast(getString(res_ip_str_no_camera_permission));
            }
        }
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == res_btn_ok) {
            Intent intent = new Intent();
            intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
            intent.putExtra(ImagePicker.EXTRAS_ISORIGIN, isOrigin);
            setResult(ImagePicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
        } else if (id == res_ll_dir) {
            if (mImageFolders == null) {
                Log.i("ImageGridActivity", "您的手机没有图片");
                return;
            }
            //点击文件夹按钮
            createPopupFolderList();
            mImageFolderAdapter.refreshData(mImageFolders);  //刷新数据
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.showAtLocation(mFooterBar, Gravity.NO_GRAVITY, 0, 0);
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = mImageFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.setSelection(index);
            }
        } else if (id == res_btn_preview) {
            Intent intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getSelectedImages());
            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
            intent.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else if (id == res_btn_back) {
            //点击返回按钮
            finish();
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mImageFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mImageFolderAdapter.setSelectIndex(position);
                imagePicker.setCurrentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();
                ImageFolder imageFolder = (ImageFolder) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
//                    mImageGridAdapter.refreshData(imageFolder.images);
                    mRecyclerAdapter.refreshData(imageFolder.images);
                    mtvDir.setText(imageFolder.name);
                }
            }
        });
        mFolderPopupWindow.setMargin(mFooterBar.getHeight());
    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        imagePicker.setImageFolders(imageFolders);
        if (imageFolders.size() == 0) {
//            mImageGridAdapter.refreshData(null);
            mRecyclerAdapter.refreshData(null);
        } else {
//            mImageGridAdapter.refreshData(imageFolders.get(0).images);
            mRecyclerAdapter.refreshData(imageFolders.get(0).images);
        }
//        mImageGridAdapter.setOnImageItemClickListener(this);
        mRecyclerAdapter.setOnImageItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        if (mRecyclerView.getItemDecorationCount() < 1) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 2), false));
        }
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mImageFolderAdapter.refreshData(imageFolders);
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        //根据是否有相机按钮确定位置
        position = imagePicker.isShowCamera() ? position - 1 : position;
        if (imagePicker.isMultiMode()) {
            Intent intent = new Intent(ImageGridActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);

            /**
             * 2017-03-20
             *
             * 依然采用弱引用进行解决，采用单例加锁方式处理
             */

            // 据说这样会导致大量图片的时候崩溃
//            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getCurrentImageFolderItems());

            // 但采用弱引用会导致预览弱引用直接返回空指针
            DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, imagePicker.getCurrentImageFolderItems());
            intent.putExtra(ImagePreviewActivity.ISORIGIN, isOrigin);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
        } else {
            imagePicker.clearSelectedImages();
            imagePicker.addSelectedImageItem(position, imagePicker.getCurrentImageFolderItems().get(position), true);
            if (imagePicker.isCrop()) {
                Intent intent = new Intent(ImageGridActivity.this, ImageCropActivity.class);
                startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //进入老版本裁剪界面
            } else {
                Intent intent = new Intent();
                intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                finish();
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            mBtnOk.setText(getString(res_ip_select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
            mBtnOk.setEnabled(true);
            mBtnPre.setEnabled(true);
            mBtnPre.setText(getResources().getString(res_ip_preview_count, imagePicker.getSelectImageCount()));
            mBtnPre.setTextColor(ContextCompat.getColor(this, res_ip_text_primary_inverted));
            mBtnOk.setTextColor(ContextCompat.getColor(this, res_ip_text_primary_inverted));
        } else {
            mBtnOk.setText(getString(res_ip_complete));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
            mBtnPre.setText(getResources().getString(res_ip_preview));
            mBtnPre.setTextColor(ContextCompat.getColor(this, res_ip_text_secondary_inverted));
            mBtnOk.setTextColor(ContextCompat.getColor(this, res_ip_text_secondary_inverted));
        }

        if (mCbOrigin.isChecked()) {
            long size = 0;
            for (ImageItem imageItem : imagePicker.getSelectedImages())
                size += imageItem.size;
            String fileSize = Formatter.formatFileSize(this, size);
            mCbOrigin.setText(getString(res_ip_origin_size, fileSize));
        }
//        mImageGridAdapter.notifyDataSetChanged();
//        mRecyclerAdapter.notifyItemChanged(position); // 17/4/21 fix the position while click img to preview
//        mRecyclerAdapter.notifyItemChanged(position + (imagePicker.isShowCamera() ? 1 : 0));// 17/4/24  fix the position while click right bottom preview button
        for (int i = imagePicker.isShowCamera() ? 1 : 0; i < mRecyclerAdapter.getItemCount(); i++) {
            if (mRecyclerAdapter.getItem(i).path != null && mRecyclerAdapter.getItem(i).path.equals(item.path)) {
                mRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                mCbOrigin.setChecked(isOrigin);
            } else {
                //从拍照界面返回
                //点击 X , 没有选择照片
                if (data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
                    //什么都不做 直接调起相机
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(ImagePicker.RESULT_CODE_ITEMS, data);
                }
                finish();
            }
        } else {
            //如果是拍照，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());

                /**
                 * 2017-03-21 对机型做旋转处理
                 */
                String path = imagePicker.getTakeImageFile().getAbsolutePath();

                //照相回来，首先添加至已选列表中
                ImageItem imageItem = new ImageItem();
                imageItem.path = path;
                imagePicker.addSelectedImageItem(0, imageItem, true);
                //是否需要裁剪，单选模式才支持裁剪
                if (!imagePicker.isMultiMode()) {
                    if (imagePicker.isCrop()) {
                        Intent intent = new Intent(ImageGridActivity.this, ImageCropActivity.class);
                        startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //进入老版本裁剪界面
                        return;
                    }
                }

                Intent intent = new Intent();
                intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                finish();
            } else if (directPhoto) {
                finish();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == res_cb_origin) {
            if (isChecked) {
                long size = 0;
                for (ImageItem item : imagePicker.getSelectedImages())
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

}
