package com.giants.imagepicker;
import android.content.Intent;
import com.giants.imagepicker.ImagePicker;
import com.giants.imagepicker.bean.ImageItem;
import com.giants.imagepicker.ui.ImageGridActivity;
import com.giants.imagepicker.view.CropImageView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ImagePickerMain extends CordovaPlugin {

    private static final String TAG = "MultiImagesPicker";

    private CallbackContext callbackContext;
    private JSONObject params;
    private ImagePicker imagePicker = ImagePicker.getInstance();
    ArrayList<ImageItem> images = null;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器

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
        this.params = args.getJSONObject(0);

        if (action.equals("getPictures")) {

            Intent intent = new Intent(cordova.getActivity().getApplicationContext(), ImageGridActivity.class);
            intent.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
            //ImagePicker.getInstance().setSelectedImages(images);
            cordova.startActivityForResult((CordovaPlugin)this,intent, 100);


            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList imagePath = new ArrayList();
                for (Iterator it = images.iterator(); it.hasNext();) {
                    ImageItem imageItem = (ImageItem)it.next();

                    imagePath.add(imageItem.path);
                }
                images.clear();

                JSONArray res = new JSONArray(imagePath);

                this.callbackContext.success(res);

            }
        }
    }
}
