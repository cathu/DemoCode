# 场景过渡动画示例

@[TOC]

## 一、场景过渡动画（Scene Transition）动画简介
>借助 Android 的过渡框架，您只需提供起始布局和结束布局，即可为界面中的各种运动添加动画效果。您可以选择所需的动画类型（例如，淡入/淡出视图或更改视图尺寸），而过渡框架会确定如何为从起始布局到结束布局的运动添加动画效果。——亲爸爸谷歌

简单粗俗的讲：您只管提供布局文件，我们帮您自动动画效果。

我们先看看效果（因为代码并不重要，所以本章不会具体讲解如何实现，[Demo戳这里](https://github.com/cathu/DemoCode/tree/master/%E5%9C%BA%E6%99%AF%E8%BF%87%E6%B8%A1%E5%8A%A8%E7%94%BB%E7%A4%BA%E4%BE%8B)）：
![效果](https://img-blog.csdnimg.cn/20200218164835908.gif)
<font color=red>**Android 的过渡框架是个很牛逼的框架，你只需要简单的几行代码就可以实现炫酷的特效，其牛逼之处在于封装的非常非常非常好！**</font>

<font color=red>**但是也有一些坑……**</font>

希望我能够描述的清楚。这个内容我希望后期会单独开一篇源码系列，揭开它的神秘面纱……
## 二、角色
1. **Scene**

这是“场景”的意思，“场景”一词运用在很多的地方当中，在Unit3D中也是扮演一个很重要的角色。

在Android中，通过源码我们可以知道**Scene**的职责：
**Scene**持有一个**ViewGroup**和一个**LayoutId**，这个**ViewGroup**可以看作是场景的**画板**，而这个**LayoutId**就是我们要显示到**画板**上的**布局**。

2. **Transition**

**Transition**包含在有关**场景**变换的时候，其动画信息。

即场景变化的过程动画需要靠**Transition**来实现。**Transition**是个**抽象类**，系统也为我们提供了很多常用的变换动画：
![继承图](https://img-blog.csdnimg.cn/20200218201035233.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NhdHppZmVuZw==,size_16,color_FFFFFF,t_70)

3. **TransitionManager**

当**场景**发生变化的时候，**TransitionManager**用来管理**Transition**集合，并且**触发动画**。

## 三、Scene

#### 1、准备一下……
我们先创建两个布局文件。
<font color=red>**scene_1.xml**</font>：
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
	... ...
    android:id="@+id/scene1">
    ... ...

</androidx.constraintlayout.widget.ConstraintLayout>
```
<font color=red>**scene_2.xml**</font>：
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
	... ...
    android:id="@+id/scene2">
    ... ...

</androidx.constraintlayout.widget.ConstraintLayout>
```
#### 2、细节详解
前面有介绍过了，**Scene**有两个关键的东西：**ViewGroup**和**LayoutId**。

**LayoutId**很好解决，就是布局文件，也就是你想显示在**场景**中的内容，随你怎么创建。

而**ViewGroup**有点文章了。在上面，我们将这个**ViewGroup**理解成一个**画布**，用来承载**LayoutId**里面的内容。这里我们分两种情况：
- **在Activity中**
我们可以有两个选择，来作为**ViewGroup**：

	1、一个就是Activity自身的contentView
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scene_1)

        rootView = findViewById(R.id.scene1)
	}
```
2、另一个就是DecorView中的content
```kotlin
		...
		rootView = findViewById(android.R.id.content)
```

这两种方式都可以实现效果，那么他们有什么区别呢？<font color=orange>第二种方式的性能会更好，原因是少了一层嵌套</font>。

**当发生过渡动画<font color=red>后</font>，其布局的区别**：
![1](https://img-blog.csdnimg.cn/20200218212945981.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NhdHppZmVuZw==,size_16,color_FFFFFF,t_70 =350x350)![2](https://img-blog.csdnimg.cn/20200218213158238.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NhdHppZmVuZw==,size_16,color_FFFFFF,t_70 =350x350)


- **在Fragment中**
受限于Fragment的实现过程，我们**只能选择其创建的View**：

```kotlin
		//此处使用的是 Kotlin，view = getView()
		rootView = view as ViewGroup
```

#### 3、创建Scene
总的来说，创建**Scene**有两种方式：

1、直接new一个，**Scene**对外提供了两种构造方法：

```java
	//创建一个没有变化的场景，一般不会使用这个
    public Scene(@NonNull ViewGroup sceneRoot) {
        mSceneRoot = sceneRoot;
    }
	
	//这个是常用的
    public Scene(@NonNull ViewGroup sceneRoot, @NonNull View layout) {
        mSceneRoot = sceneRoot;
        mLayout = layout;
    }
```
2、使用**Scene**的静态方法：

```java
    public static Scene getSceneForLayout(@NonNull ViewGroup sceneRoot,
     @LayoutRes int layoutId,@NonNull Context context) {
      ... ...
    }
```
#### 4、其他
其实**Scene**提供了两个静态方法，一个就是上面描述的 `getSceneForLayout()` 用于创建**Scene**，另一个就是 `getCurrentScene()` 用于获取当前的**ViewGroup**的**Scene**，其具体的实现是通过`View.setTag()`和`View.getTag()`来实现的。

## 四、Transition
前面有介绍**Transition** 是个抽象类，负责管理动画具体的实现。

它主要做了两件事情：**1、捕获View的属性值；2、根据捕获的属性值来执行动画。**

#### 1、创建

我们可以有两种方式实现：
- 直接new 

```kotlin
private val transition1 = ChangeBounds()
private val transition2 = Fade()
```
- 在资源文件中定义

首先需要在**res**目录中创建**transition**目录，然后在**res/transition**中创建资源文件。
例如，**Fade()** 对应的根标签为 **\<fade\>**，**ChangeBounds()** 对应的根标签为 **\<changeBounds>**。
最后在代码中初始化：

```kotlin
val transition = 
TransitionInflater.from(context).inflateTransition(R.transition.ts_fade)
```
#### 2、TransitionSet
当然，我们也可以借助**TransitionSet**来进行多个**Transition**的组合。

```kotlin
val transitionSet = TransitionSet()
transitionSet .addTransition(ChangeBounds())
transitionSet .addTransition(Fade(Fade.IN))
```

#### 3、自定义Transition
如果你不满足系统自定义的**Transition**，那么你完全可以自定义。

由于本章主要以使用为讲解，另开单章，戳这里。

## 五、TransitionManager
通过**TransitionManager**我们才能够驱动**Transition**来进行**Scene**的过渡动画。

它有两种方式：
1、`TransitionManager.beginDelayedTransition()`
2、`TransitionManager.go()`

第一种方式用于无**Scene**的情况下，适用于在同一布局下，其子View的变化而产生的过渡动画。

第二种方式就是用于**Scene**了。

#### 1、beginDelayedTransition
使用这种方式的话，就完全用不上**Scene**，使用的话也很简单：

<font color=orange>在你的布局发生变化之前调用`TransitionManager.beginDelayedTransition(ViewGroup,Transition?)`即可。</font>

例如：

```kotlin
	val rootView = findViewById(R.id.scene1)
	val fadeTransition = Fade(Fade.IN)
	val textView = TextView(context).apply{
		text = "你好"
	}
	TransitionManager.beginDelayedTransition(rootView, fadeTransition)
    rootView.addView(textView)
```

#### 2、go
这个方法才是我们的主角……

它有两个重载：
1、`go(Scene scene)`
2、`go(Scene scene, Transition transition)`

调用第一个方法的话，就会使用默认的**Transition**：
`private static Transition sDefaultTransition = new AutoTransition();`

使用的话也非常的简单，将我们创建的**Scene**传入，再传入**Transition**即可完成过渡动画。

## 六、动画监听
该动画的监听是由**Transition**来完成的，调用
`Transition.addListener(Transition.TransitionListener)`即可，**Transition.TransitionListener**的几个方法：
- onTransitionStart  过渡动画开始时调用
- onTransitionEnd  过渡动画结束时调用
- onTransitionCancel  过渡动画取消是调用
- onTransitionPause  过渡动画暂停时调用
- onTransitionResume  过渡动画恢复时调用

利用这些监听方法，我们可以完成一些必要的操作。

## 七、注意事项（避免踩坑）

1、如果你尝试使用两种不同的布局来进行场景过渡动画，注意子View的id必须相同，否则没有过渡动画效果（会闪现）。

2、当进行了一次两种不同布局的场景过渡动画，原**Scene**不会失效，但是其子View会刷新，需要重新赋值！

3、限制。对于**SurfaceView**和**Texture**可能无法正确显示；继承于**AdapterView**的视图会以与过渡框架不兼容的方式管理它们的子视图，可能会挂掉，例如：**ListView、ExpandableListView、Spinner等……**
