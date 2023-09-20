package ru.netology.statsviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Scene
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<ViewGroup>(R.id.root)

        val scene = Scene.getSceneForLayout(root, R.layout.end_scene, this)

        findViewById<View>(R.id.goButton).setOnClickListener {
            TransitionManager.go(scene)
        }

    }
}