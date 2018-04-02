## 安装

 - `cordova plugin add https://github.com/giantss/cordova-plugin-ImagePicker.git`
 
 或者
 
 - `cordova plugin add cordova-plugin-imagepicker-plus`


## 使用
- `cd /cordova-plugin-ImagePicker/tree/master/example/cordova/ImagePickerDemo/`


- `cordova run android/ios`

## 注：如果执行`cordova run ios`出现
```
** BUILD SUCCEEDED **

No target specified for emulator. Deploying to undefined simulator
Device type "com.apple.CoreSimulator.SimDeviceType.undefined" could not be found.

```
用xcode打开`/cordova-plugin-ImagePicker/tree/master/example/cordova/ImagePickerDemo/platforms/ios／HelloCordova.xcworkspace`文件 手动运行模拟器或者真机（运行真机需要的环境这里不多做介绍）
