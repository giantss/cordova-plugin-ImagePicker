package com.giants.imagepicker;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.giants.imagepicker.bean.ImageItem;
import com.giants.imagepicker.ui.ImageGridActivity;
import com.giants.imagepicker.view.CropImageView;
import com.giants.imagepicker.imageloader.*;
import com.nanchen.compresshelper.CompressHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import top.zibin.luban.Luban;

/**
 * Created by Administrator on 2017/8/29.
 */
public class ImagePickerMain extends CordovaPlugin {

    private static final String TAG = "MultiImagesPicker";

    private int image_limit_width = 0;
    private int image_limit_height = 0;
    private int image_limit_quality = 0;

    private CallbackContext callbackContext;
    private ImagePicker imagePicker = ImagePicker.getInstance();
    ArrayList<ImageItem> images = null;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器

        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        JSONObject params = args.getJSONObject(0);

        if (action.equals("getPictures")) {
            imagePicker.setSelectLimit(params.getInt("maximumImagesCount"));
            image_limit_width = params.getInt("width");
            image_limit_height = params.getInt("height");
            image_limit_quality = params.getInt("quality");

            Intent intent = new Intent(cordova.getActivity().getApplicationContext(), ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
            cordova.startActivityForResult((CordovaPlugin)this,intent, 100);


            return true;
        }
        else if (action.equals("takePhoto")) {
            image_limit_width = params.getInt("width");
            image_limit_height = params.getInt("height");
            image_limit_quality = params.getInt("quality");

            Intent intent = new Intent(cordova.getActivity().getApplicationContext(), ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
            cordova.startActivityForResult((CordovaPlugin)this,intent, 100);


            return true;
        }

        return false;
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if(data == null) {
                Context appContext = this.cordova.getActivity().getApplicationContext();
                Resources resource = appContext.getResources();
                String pkgName = appContext.getPackageName();
                int res_canceled = resource.getIdentifier("canceled", "string", pkgName);
                String canceled = resource.getString(res_canceled);
                this.callbackContext.error(canceled);
            }
            else if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {

                Context context = cordova.getActivity().getApplicationContext();

                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                boolean isOrigin = data.getBooleanExtra(ImagePicker.EXTRAS_ISORIGIN, false); // 原图

                File targetDir = new File(context.getCacheDir(), "ImagePicker");
                if(!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                String targetDirPath = targetDir.getAbsolutePath();

                ArrayList imageObjects = new ArrayList();

                for (Iterator it = images.iterator(); it.hasNext(); ) {
                    ImageItem imageItem = (ImageItem) it.next();

                    if(TextUtils.isEmpty(imageItem.path)) continue;;

                    String newPath = "";
                    int newWidth = -1;
                    int newHeight = -1;
                    long newSize = -1;

                    if(isOrigin) {
                        newPath = imageItem.path;
                        newWidth = imageItem.width;
                        newHeight = imageItem.height;
                        newSize = imageItem.size;
                    }
                    else {
                        // do not compress gif
                        if(imageItem.path.toLowerCase().endsWith(".gif")) {
                            newPath = imageItem.path;
                            newWidth = imageItem.width;
                            newHeight = imageItem.height;
                            newSize = imageItem.size;
                        }
                        else { // 压缩
                            File oldFile = new File(imageItem.path);

                            Log.v(TAG, "Image size before compression =====> " + readableFileSize(oldFile.length()));

                            File newFile = null;

                            if(image_limit_width > 0 && image_limit_height > 0 && image_limit_quality > 0) {
                                newFile = new CompressHelper.Builder(context)
                                        .setMaxWidth(image_limit_width)  // 默认最大宽度
                                        .setMaxHeight(image_limit_height) // 默认最大高度
                                        .setQuality(image_limit_quality)    // 默认压缩质量
                                        .setDestinationDirectoryPath(targetDirPath)
                                        .build()
                                        .compressToFile(oldFile);
                            }
                            else { // auto compress like wechat
                                try {
                                    List<File> files = Luban.with(context)
                                            .load(oldFile)
                                            .setTargetDir(targetDirPath)
                                            .get();
                                    if(files.size() > 0) {
                                        newFile = files.get(0);
                                    }
                                    else {
                                        throw new Exception("Unknown exception when compressing " + oldFile.getAbsolutePath());
                                    }
                                }
                                catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if(newFile != null) {
                                newPath = newFile.getAbsolutePath();
                                newSize = newFile.length();

                                BitmapFactory.Options bounds = new BitmapFactory.Options();
                                bounds.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(newPath, bounds);
                                newHeight = bounds.outHeight;
                                newWidth = bounds.outWidth;

                                Log.v(TAG, "Image size after compression =====> " + readableFileSize(newSize));
                            }
                        }
                    }

                    if(!TextUtils.isEmpty(newPath)) {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("path", newPath);
                            obj.put("width", newWidth);
                            obj.put("height", newHeight);
                            obj.put("size", newSize);

                            imageObjects.add(obj);
                        } catch (Exception e) {

                            Log.getStackTraceString(e);
                        }
                    }
                }

                images.clear();

                try {
                    JSONArray images = new JSONArray(imageObjects);

                    JSONObject obj = new JSONObject();
                    obj.put("images", images);
                    obj.put("isOrigin", isOrigin);

                    this.callbackContext.success(obj);
                } catch (Exception e) {

                    Log.getStackTraceString(e);
                }

            }
        }
    }
}
