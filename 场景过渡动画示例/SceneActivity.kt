package com.cathu.bluetoothproject.module.anim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.transition.*
import com.cathu.bluetoothproject.R
import com.cathu.bluetoothproject.util.SimpleTransitionListener

class SceneActivity : AppCompatActivity() {

    //场景的SceneRoot
    private lateinit var rootView:ViewGroup
    //两个布局的场景
    private lateinit var scene1: Scene
    private lateinit var scene2: Scene
    //开始按钮
    private lateinit var btnStart : View
    //返回按钮
    private lateinit var btnBack : View

    //两个Transition
    private val transition1 :Transition = ChangeBounds()
    private val transition2 :Transition = ChangeBounds()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scene_1)

        rootView = findViewById(android.R.id.content)

        scene2 = Scene.getSceneForLayout(rootView, R.layout.scene_2, this)
        scene1 = Scene.getSceneForLayout(rootView, R.layout.scene_1, this)

        transition1.addListener(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                scene2Setting()
            }
        })

        transition2.addListener(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                scene1Setting()
            }
        })

        scene1Setting()
    }


    /**
     * 第一个场景的设置
     */
    private fun scene1Setting(){
        btnStart = rootView.findViewById(R.id.btn_start)
        btnStart.setOnClickListener {
            TransitionManager.go(scene2, transition1)
        }
    }

    /**
     * 第二个场景的设置
     */
    private fun scene2Setting(){
        btnBack = rootView.findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            TransitionManager.go(scene1,transition2)
        }
    }
}
