package com.giants.imagepicker;

import com.giants.imagepicker.bean.ImageItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 新的DataHolder，使用单例和弱引用解决崩溃问题
 * <p>
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-20  07:01
 */
public class DataHolder {
    public static final String DH_CURRENT_IMAGE_FOLDER_ITEMS = "dh_current_image_folder_items";

    private static com.giants.imagepicker.DataHolder mInstance;
    private Map<String, List<ImageItem>> data;

    public static com.giants.imagepicker.DataHolder getInstance() {
        if (mInstance == null){
            synchronized (com.giants.imagepicker.DataHolder.class){
                if (mInstance == null){
                    mInstance = new com.giants.imagepicker.DataHolder();
                }
            }
        }
        return mInstance;
    }

    private DataHolder() {
        data = new HashMap<>();
    }

    public void save(String id, List<ImageItem> object) {
        if (data != null){
            data.put(id, object);
        }
    }

    public Object retrieve(String id) {
        if (data == null || mInstance == null){
            throw new RuntimeException("你必须先初始化");
        }
        return data.get(id);
    }
}
