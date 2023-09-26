package ru.netology.statsviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Scene
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import ru.netology.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<ViewGroup>(R.id.root)
        val scene = Scene.getSceneForLayout(root, R.layout.end_scene, this)

        findViewById<Button>(R.id.goButton).setOnClickListener {
            TransitionManager.go(scene, ChangeBounds().apply {
                duration = 1000
            })
        }
    }
}