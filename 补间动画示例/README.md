# 补间动画示例

## FoldLayout
 
### 前言
因为补间动画并没有X轴方向的旋转，所以以缩放代替。关于自定义Animation可以自行百度查阅相关资料。

### 用法

```kotlin
    foldLayout.setControlView(Button(context).apply { text="关闭" })
    foldLayout.addView(Button(context).apply { text="你好1" })
    foldLayout.addView(Button(context).apply { text="你好2" })
    foldLayout.addView(Button(context).apply { text="你好3" })
    //应该可以自行添加各种View
```
当然还有各种小问题啦，此例只作展示，意思意思下。
### 效果
![点击查看效果](https://github.com/cathu/DemoCode/blob/master/%E8%A1%A5%E9%97%B4%E5%8A%A8%E7%94%BB%E7%A4%BA%E4%BE%8B/foldGIF.gif?raw=true)
