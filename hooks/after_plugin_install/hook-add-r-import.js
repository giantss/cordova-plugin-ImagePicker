#!/usr/bin/env node
/*
A hook to add R.java to the draw activiy in Android platform. 
*/

var fs   = require('fs');
var path = require('path');

var rootdir = process.argv[2];

function replace_string_in_file(filename, to_replace, replace_with) {
    var data   = fs.readFileSync(filename, 'utf8');
    var result = data.replace(to_replace, replace_with);
    fs.writeFileSync(filename, result, 'utf8');
}

var target = "stage";
if (process.env.TARGET) {
    target = process.env.TARGET;
}

var ourconfigfile = path.join("plugins", "android.json");
var configobj     = JSON.parse(fs.readFileSync(ourconfigfile, 'utf8'));
// Add java files where you want to add R.java imports in the following array

var filestoreplace = [
    "platforms/android/src/com/giants/imagepicker/GApp.java",
    "platforms/android/src/com/giants/imagepicker/GlideImageLoader.java",
    "platforms/android/src/com/giants/imagepicker/ImageDataSource.java",
    "platforms/android/src/com/giants/imagepicker/PicassoImageLoader.java",
    "platforms/android/src/com/giants/imagepicker/adapter/ImageFolderAdapter.java",
    "platforms/android/src/com/giants/imagepicker/adapter/ImageGridAdapter.java",
    "platforms/android/src/com/giants/imagepicker/adapter/ImagePageAdapter.java",
    "platforms/android/src/com/giants/imagepicker/adapter/ImageRecyclerAdapter.java",
    "platforms/android/src/com/giants/imagepicker/ui/ImageBaseActivity.java",
    "platforms/android/src/com/giants/imagepicker/ui/ImageCropActivity.java",
    "platforms/android/src/com/giants/imagepicker/ui/ImageGridActivity.java",
    "platforms/android/src/com/giants/imagepicker/ui/ImagePreviewActivity.java",
    "platforms/android/src/com/giants/imagepicker/ui/ImagePreviewBaseActivity.java",
    "platforms/android/src/com/giants/imagepicker/ui/ImagePreviewDelActivity.java",
    "platforms/android/src/com/giants/imagepicker/view/CropImageView.java",
    "platforms/android/src/com/giants/imagepicker/view/FolderPopUpWindow.java",

];
filestoreplace.forEach(function (val, index, array) {
    if (fs.existsSync(val)) {
        console.log("Android platform available !");
        //Getting the package name from the android.json file,replace with your plugin's id
        var packageName = configobj.installed_plugins["cordova-plugin-ImagePicker"]["PACKAGE_NAME"];
        console.log("With the package name: " + packageName);
        console.log("change com.your.package.name for " + packageName);
        replace_string_in_file(val, /com.your.package.name/g, packageName);

    } else {
        console.log("No android platform found! :(");
    }
});
