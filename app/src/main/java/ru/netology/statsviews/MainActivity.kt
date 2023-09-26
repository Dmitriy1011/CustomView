package ru.netology.statsviews

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Scene
import android.transition.TransitionManager
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.Button
import ru.netology.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<ViewGroup>(R.id.root)
        root.layoutTransition = LayoutTransition().apply {
            setDuration(2000)
            setInterpolator(LayoutTransition.CHANGE_APPEARING, BounceInterpolator())
        }

        val btnGo = findViewById<Button>(R.id.goButton).setOnClickListener {
            val view = layoutInflater.inflate(R.layout.stats_view, root, false)
            root.addView(view, 0)
        }
    }
}