/**
 * Created by zhongpeng on 2017/5/31.
 */
var exec = require('cordova/exec');

if (typeof Object.assign != 'function') {
    Object.assign = function (target) {
        'use strict';
        if (target == null) {
            throw new TypeError('Cannot convert undefined or null to object');
        }

        target = Object(target);
        for (var index = 1; index < arguments.length; index++) {
            var source = arguments[index];
            if (source != null) {
                for (var key in source) {
                    if (Object.prototype.hasOwnProperty.call(source, key)) {
                        target[key] = source[key];
                    }
                }
            }
        }

        return target;
    };
}

var defaults = {
    maximumImagesCount: 9,
    width: -1, // negative value means auto scale
    height: -1,
    quality: 80
};

module.exports = {
    /**
     * 获取图片地址
     * @param onSuccess
     * @param onFail
     * @param params
     */
    getPictures: function (onSuccess, onFail, params) {
        var options = Object.assign({}, defaults, params);

        exec(onSuccess, onFail, 'ImagePicker', 'getPictures', [options]);
    },
    /**
     * 拍照
     * @param onSuccess
     * @param onFail
     * @param params
     */
    takePhoto: function (onSuccess, onFail, params) {
        var options = Object.assign({}, defaults, params);

        exec(onSuccess, onFail, 'ImagePicker', 'takePhoto', [options]);
    }
};