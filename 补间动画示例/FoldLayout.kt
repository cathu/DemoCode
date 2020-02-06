package com.cathu.bluetoothproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.cathu.bluetoothproject.R

/**
 *       Created by Cathu on 2020/2/6 19:42
 */
class FoldLayout : LinearLayout {

    //用于点击
    private var controlView: View? = null

    //controlView在上面或是下面
    //默认为true
    var isBottom = true

    //其他View集合
    private var otherViews = arrayListOf<View>()

    //当前需要启动动画的View的index
    private var index = 0

    //当前需要启动动画的View
    private var currentView: View? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        orientation = VERTICAL
    }


    /**
     * 设置ControlView
     */
    fun setControlView(v: View, onClick: ((View) -> Unit)? = null) {
        controlView = v
        addView(v, 0)
        controlView!!.setOnClickListener {
            //开始翻转动画
            startAnim()
            //暴露点击事件
            onClick?.let { holder -> holder(it) }
        }
    }

    /**
     * 开始动画
     */
    private fun startAnim() {
        if (orientation == HORIZONTAL) {
            throw IllegalStateException("该布局只能设置成纵向布局")
        }
        index = otherViews.size - 1
        currentView = otherViews[index]
        var anim = getAnim()
        currentView!!.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                currentView!!.visibility = View.INVISIBLE
                index -= 1
                if (index > -1) {
                    currentView = otherViews[index]
                    anim = getAnim()
                    anim.setAnimationListener(this)
                    currentView!!.startAnimation(anim)
                }
            }

            override fun onAnimationStart(animation: Animation?) {}
        })
    }

    /**
     * 获取动画实例
     */
    private fun getAnim(): Animation {
        val anim: Animation = if (isBottom) {
            AnimationUtils.loadAnimation(context, R.anim.anim_fold_bottom)
        } else {
            AnimationUtils.loadAnimation(context, R.anim.anim_fold_top)
        }
        anim.fillAfter = true
        anim.interpolator = LinearInterpolator()
        return anim
    }


    override fun addView(child: View) {
        //super.addView(child)
        otherViews.add(child)

        //纵向的时候
        if (isBottom) {
            addView(child, 0)
        } else {
            addView(child, otherViews.size)
        }
    }

}