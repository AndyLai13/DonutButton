package com.andylai.donutbuttons

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val circleAnnotationBar = CircleAnnotationBar(findViewById(R.id.circleAnnotationBar))

        circleAnnotationBar.setRedoEnabled(false)
        circleAnnotationBar.setUndoEnabled(false)

        findViewById<Button>(R.id.open).setOnClickListener {
            circleAnnotationBar.setOpen(true)
        }

        findViewById<Button>(R.id.close).setOnClickListener {
            circleAnnotationBar.setOpen(false)
        }

        findViewById<Button>(R.id.penHide).setOnClickListener {
            circleAnnotationBar.setPenVisibility(View.GONE)
        }

        findViewById<Button>(R.id.penShow).setOnClickListener {
            circleAnnotationBar.setPenVisibility(View.VISIBLE)
        }

        findViewById<Button>(R.id.brushHide).setOnClickListener {
            circleAnnotationBar.setBrushVisibility(View.GONE)
        }

        findViewById<Button>(R.id.brushShow).setOnClickListener {
            circleAnnotationBar.setBrushVisibility(View.VISIBLE)
        }
    }
}