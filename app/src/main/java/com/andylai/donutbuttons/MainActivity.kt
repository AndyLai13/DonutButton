package com.andylai.donutbuttons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val root = findViewById<ConstraintLayout>(R.id.root)
        val donutButtonView = findViewById<DonutButtonsView>(R.id.donutButtonView)
        CircleBar(this, root, donutButtonView)
    }
}