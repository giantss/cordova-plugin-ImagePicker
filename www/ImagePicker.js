/**
 * Created by zhongpeng on 2017/5/31.
 */
var exec = require('cordova/exec');

module.exports = {
    /**
     * 获取图片地址
     * @param onSuccess
     * @param onFail
     * @param params
     */
    getPictures: function(onSuccess, onFail, params) {
        exec(onSuccess, onFail, "ImagePicker", "getPictures", [params]);
    }
};
