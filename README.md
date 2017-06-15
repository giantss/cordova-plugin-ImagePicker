# cordova-plugin-ImagePicker
非常感谢<a href="https://github.com/nanchen2251">南尘</a>和 <a href="https://github.com/banchichen">banchichen</a> 提供的源码支持

android  一个支持多选，实现了拍照、压缩等功能
ios 一个支持多选、拍照、选图片选择器，同时有预览功能，支持iOS6+。
##主要功能
- 选择获取多张图片地址

##安装要求
- Cordova Version >=3.5
- Cordova-Android >=4.0
- Cordova-iOS >=6.0


ios Requirements 要求

OS 6 or later. Requires ARC
iOS6及以上系统可使用. ARC环境.

When system version is iOS6 or iOS7, Using AssetsLibrary.
When system version is iOS8 or later, Using PhotoKit.
如果运行在iOS6或7系统上，用的是AssetsLibrary库获取照片资源。
如果运行在iOS8及以上系统上，用的是PhotoKit库获取照片资源。


##安装
1. 命令行运行      ```cordova plugin add https://github.com/giantss/cordova-plugin-ImagePicker.git```
2. 命令行运行 cordova build --device

##注意事项
1. 这个插件要求cordova-android 的版本 >=4.0,推荐使用 cordova  5.0.0 或更高的版本，因为从cordova 5.0 开始cordova-android 4.0 是默认使用的android版本
2. 请在cordova的deviceready事件触发以后再调用本插件！！！


## 2.功能和参数含义

### 温馨提示:目前库中的预览界面有个原图的复选框,暂时只做了UI,还没有做压缩的逻辑

<table>
  <tdead>
    <tr>
      <th align="center">配置参数</th>
      <th align="center">参数含义</th>
    </tr>
  </tdead>
  <tbody>
    <tr>
      <td align="center">multiMode</td>
      <td align="center">图片选着模式，单选/多选</td>
    </tr>
    <tr>
      <td align="center">selectLimit</td>
      <td align="center">多选限制数量，默认为9</td>
    </tr>
    <tr>
      <td align="center">width</td>
      <td align="center">设置图片的width，默认为720</td>
    </tr>

     <tr>
          <td align="center">height</td>
          <td align="center">设置图片的height，默认为960</td>
     </tr>
    <tr>
          <td align="center">quality</td>
          <td align="center">图片质量 默认100</td>
     </tr>

  </tbody>
</table>

## android 演示
 ![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo1.png)
 ![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo2.gif)
 ![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo3.gif)
 ![image](https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo5.gif)

## ios效果图
![image](https://github.com/banchichen/TZImagePickerController/blob/master/TZImagePickerController/ScreenShots/photoPickerVc.PNG)


##使用方式

```Javascript
ImagePicker.getPictures(function(result) {
    alert(result);
}, function(err) {
    alert(err);
}, { max : 9, width : 720, height : 960, quality : 100 });

```

