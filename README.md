# cordova-plugin-ImagePicker
非常感谢<a href="https://github.com/nanchen2251">南尘</a>和 <a href="https://github.com/banchichen">banchichen</a> 提供的源码支持 多点star✨开源不宜，谢谢。

一个支持多选，相册实现了拍照、预览、（android 图片压缩）等功能

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


##安装要求
- Cordova Version >=5.0
- Cordova-Android >=4.0
- Cordova-iOS >=6.0


##ios Requirements 要求

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


## android视频演示

<a href="http://oqdxjvpc7.bkt.clouddn.com/111.mp4" target="_blank">点击查看视频(mp4格式)</a><br>
<a href="http://v.youku.com/v_show/id_XMjg0NDg0NDIyMA==.html" target="_blank">点击查看视频(优酷)</a>

## ios视频演示

<a href="http://oqdxjvpc7.bkt.clouddn.com/ios1.mp4" target="_blank">点击查看视频(mp4格式)</a><br>
<a href="http://v.youku.com/v_show/id_XMjg0NDg0NTU4OA==.html" target="_blank">点击查看视频(优酷)</a>

## android效果图

 
 <img src="https://github.com/jeasonlzy/Screenshots/blob/master/ImagePicker/demo2.gif" width="270px" height="480">


## ios效果图

 <img src="https://github.com/banchichen/TZImagePickerController/blob/master/TZImagePickerController/ScreenShots/photoPickerVc.PNG" width="270px" height="480">


##使用方式

```Javascript
ImagePicker.getPictures(function(result) {
    alert(result);
}, function(err) {
    alert(err);
}, { max : 9, width : 720, height : 960, quality : 100 });

```

