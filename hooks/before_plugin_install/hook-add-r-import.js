#!/usr/bin/env node
/*
A hook to add R.java to the draw activiy in Android platform. 
*/

module.exports = function (context) {

    var fs   = require('fs');
    var path = require('path');

    function replace_string_in_file(filename, to_replace, replace_with) {
        var data   = fs.readFileSync(filename, 'utf8');
        var result = data.replace(to_replace, replace_with);
        fs.writeFileSync(filename, result, 'utf8');
    }

    var platformRoot = context.opts.projectRoot;

    var ConfigParser = null;
    try {
        ConfigParser = context.requireCordovaModule('cordova-common').ConfigParser;
    } catch (e) {
        // fallback
        ConfigParser = context.requireCordovaModule('cordova-lib/src/configparser/ConfigParser');
    }

    //Getting the package name from the android.json file,replace with your plugin's id
    var config      = new ConfigParser(path.join(context.opts.projectRoot, "config.xml"));
    var packageName = config.android_packageName() || config.packageName();//获取包名
    console.log("packageName: " + packageName);

    // Add java files where you want to add R.java imports in the following array
    var filestoreplace = [
        "/plugins/cordova-plugin-imagepicker-plus/src/android/GApp.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/PicassoImageLoader.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/GlideImageLoader.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ImageDataSource.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/adapter/ImageFolderAdapter.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/adapter/ImageGridAdapter.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/adapter/ImagePageAdapter.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/adapter/ImageRecyclerAdapter.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ui/ImageBaseActivity.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ui/ImageCropActivity.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ui/ImageGridActivity.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ui/ImagePreviewActivity.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ui/ImagePreviewBaseActivity.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/ui/ImagePreviewDelActivity.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/view/CropImageView.java",
        "/plugins/cordova-plugin-imagepicker-plus/src/android/module/view/FolderPopUpWindow.java"

    ];
    filestoreplace.forEach(function (val) {

        var filePath = platformRoot + val;

        if (fs.existsSync(filePath)) {

            // console.log("Android platform available !");
            // console.log("With the package name: " + packageName);
            // console.log("change com.your.package.name for " + packageName);

            replace_string_in_file(filePath, /com.your.package.name/g, packageName);
        } else {
            console.log("No android platform found! :(");
        }
    });

};