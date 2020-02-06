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
### 效果
