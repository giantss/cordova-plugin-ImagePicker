#import <Cordova/CDVPlugin.h>
#import "TZImagePickerController.h"
#import "UIView+Layout.h"
#import "TZTestCell.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import <Photos/Photos.h>
#import "LxGridViewFlowLayout.h"
#import "TZImageManager.h"
#import "TZVideoPlayerController.h"
#import "TZPhotoPreviewController.h"
#import "TZGifPhotoPreviewController.h"
#import "TZLocationManager.h"




@interface ImagePicker : CDVPlugin

@property(nonatomic, copy) NSString *callback;
@property (nonatomic, strong) UIImagePickerController *imagePickerVc;
@property (nonatomic, strong) UICollectionView *collectionView;
@property (strong, nonatomic) CLLocation *location;
// 设置开关
@property (nonatomic, assign) BOOL enableShowTakePhoto; ///< 在内部显示拍照按钮
@property (nonatomic, assign) BOOL enableShowTakeVideo; ///< 在内部显示拍视频按钮
@property (nonatomic, assign) BOOL enableSortAscending;     ///< 照片排列按修改时间升序
@property (nonatomic, assign) BOOL enablePickingVideo; ///< 允许选择视频
@property (nonatomic, assign) BOOL enablePickingImage; ///< 允许选择图片
@property (nonatomic, assign) BOOL enablePickingGif;   ///< 允许选择GIF图片
@property (nonatomic, assign) BOOL enablePickingOriginalPhoto; ///< 允许选择原图
@property (nonatomic, assign) BOOL enableShowSheet; ///< 显示一个sheet,把拍照按钮放在外面
    
@property (nonatomic, assign) NSUInteger  maxCountTF;  ///< 照片最大可选张数，设置为1即为单选模式
@property (nonatomic, assign) NSUInteger  columnNumberTF; ///< 每行显示的照片张数
@property (nonatomic, assign) BOOL enableCrop; ///< 单选模式下裁剪
@property (nonatomic, assign) BOOL enableCircleCrop; ///< 使用圆形裁剪框
@property (nonatomic, assign) BOOL enablePickingMuitlpleVideo; ///< 允许多选视频
@property (nonatomic, assign) BOOL enableSelectedIndex; ///< 是否显示图片序号
    
@property (nonatomic, assign) NSInteger width;
@property (nonatomic, assign) NSInteger height;
@property (nonatomic, assign) NSInteger quality;

- (void)getPictures:(CDVInvokedUrlCommand *)command;
- (NSString *)saveAndGetImageDocuments:(UIImage *)currentImage withName: (NSString *)imageName;

@end
