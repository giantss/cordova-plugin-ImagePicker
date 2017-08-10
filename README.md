# cordova-plugin-ImagePicker

非常感谢[南尘](https://github.com/nanchen2251)和 [banchichen](https://github.com/banchichen) 提供的源码支持 多点star✨开源不容易，谢谢。扣扣群：240255635

一个支持多选，相册实现了拍照、预览、（Android 图片压缩）等功能

## 功能

- 相册目录
- 多选图
- 相册内部拍照
- 预览选中的图片
- 图片压缩（Android）

## 安装要求

- Cordova Version >=5.0
- Cordova-Android >=4.0
- Cordova-iOS >=6.0

## iOS Requirements 要求

OS 6 or later. Requires ARC iOS6及以上系统可使用. ARC环境.

When system version is iOS6 or iOS7, Using AssetsLibrary. When system version is iOS8 or later, Using PhotoKit. 如果运行在iOS6或7系统上，用的是AssetsLibrary库获取照片资源。 如果运行在iOS8及以上系统上，用的是PhotoKit库获取照片资源。

## 安装

1. 命令行运行 `cordova plugin add https://github.com/giantss/cordova-plugin-ImagePicker.git`
2. 命令行运行 cordova build --device

## android注意事项

add插件到项目以后先不要直接build，执行下面的步骤

- 全局搜索插件android目录，将 `com.your.package.name` 全部替换成自己创建项目时的包名。
- build

cordova JAVA 版本 可能不够 支持 砖石运算符（7.0.1 时是这样的）
```
错误: -source 1.6 中不支持 diamond 运算符
        else imageFolders = new ArrayList<>();
                                          ^
  (请使用 -source 7 或更高版本以启用 diamond 运算符)
```
修改 Android 项目下面的 build.gradle 文件中的
```
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_6
    targetCompatibility JavaVersion.VERSION_1_6
}
```
改为
```
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_7
    targetCompatibility JavaVersion.VERSION_1_7
}
```

## Android 视频演示

[点击查看视频(mp4格式)](http://oqdxjvpc7.bkt.clouddn.com/111.mp4)<br>
[点击查看视频(优酷)](http://v.youku.com/v_show/id_XMjg0NDg0NDIyMA==.html)

## iOS 视频演示

[点击查看视频(mp4格式)](http://oqdxjvpc7.bkt.clouddn.com/ios1.mp4)<br>
[点击查看视频(优酷)](http://v.youku.com/v_show/id_XMjg0NDg0NTU4OA==.html)

## 效果图

| Android         | iOS          |
|:---------------:|:------------:|
| <img src="./res/android.png" width="270px" height="480"> | <img src="./res/ios.jpg" width="270px" height="480"> |

## 使用方式

```javascript
ImagePicker.getPictures(function(result) {
    alert(result);
}, function(err) {
    alert(err);
}, { maximumImagesCount : 9, width : 720, height : 960, quality : 100 });
```

## 参数含义

| 配置参数            | 参数含义                   |
|:------------------:|:-------------------------:|
| maximumImagesCount | 多选限制数量，默认为9        |
| width              | 设置图片的width，默认为720   |
| height             | 设置图片的height，默认为960  |
| quality            | 图片质量 默认100            |

## License

[The MIT License (MIT)](http://www.opensource.org/licenses/mit-license.html)
