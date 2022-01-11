package com.andylai.donutbuttons

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintProperties.WRAP_CONTENT
import androidx.core.content.res.ResourcesCompat
import com.andylai.donutbuttons.ColorPicker.ColorType
import com.andylai.donutbuttons.ColorPicker.RES_HIGHLIGHTER_COLORS_DRAWABLES
import com.andylai.donutbuttons.ColorPicker.RES_PEN_COLOR_DRAWABLES
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    val penColorButtons = ColorButtons(
        colorType = ColorType.Pen,
        colorDrawables = RES_PEN_COLOR_DRAWABLES,
    )
    val highlighterColorButtons = ColorButtons(
        colorType = ColorType.HighLighter,
        colorDrawables = RES_HIGHLIGHTER_COLORS_DRAWABLES,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val root = findViewById<ConstraintLayout>(R.id.root)
        val donutButtonView = findViewById<DonutButtonsView>(R.id.donutButtonView).apply {
            setPressableButtons(listOf(0, 1, 2, 7), 1)
        }.apply {
            callback = object : DonutButtonsView.Callback {
                override fun onCenterTouched() {
                    Log.d("Andy", "onCenterTouched")
                }

                override fun onArcButtonClick(index: Int) {
                    Log.d("Andy", "onArcButtonClick , index = $index")
                }

                override fun onArcButtonPressed(index: Int) {
                    Log.d("Andy", "onArcButtonPressed , index =  $index")
                    penColorButtons.resetVisibility()
                    highlighterColorButtons.resetVisibility()
                }

                override fun onArcButtonClickUnderPressed(index: Int) {
                    Log.d("Andy", "onArcButtonClickUnderPressed , index = $index")
                    if (index == 1)
                        penColorButtons.changeVisibility()
                    else if (index == 2)
                        highlighterColorButtons.changeVisibility()
                }
            }
        }.apply {
            setIconDrawables(ArrayList<Drawable>().apply {
                add(getCompatDrawable(R.drawable.ic_donut_eraser))
                add(getCompatDrawable(R.drawable.ic_donut_pen))
                add(getCompatDrawable(R.drawable.ic_donut_brush))
                add(getCompatDrawable(R.drawable.ic_donut_close))
                add(getCompatDrawable(R.drawable.ic_donut_delete))
                add(getCompatDrawable(R.drawable.ic_donut_undo))
                add(getCompatDrawable(R.drawable.ic_donut_redo))
                add(getCompatDrawable(R.drawable.ic_donut_save))
            })
        }
        initColorButtons(root, donutButtonView, penColorButtons)
        initColorButtons(root, donutButtonView, highlighterColorButtons)
    }

    /**
     * ColorButtons take move button as center to put
     * and start from degree 180 to negative and positive position
     * respectively for Pen and Highlighter.
     */
    private fun initColorButtons(
        rootView: ViewGroup,
        centerView: View,
        colorButtons: ColorButtons
    ) {
        val centerViewWidth = resources.getDimension(R.dimen.center_width)
        val radius = centerViewWidth / 2 + centerViewWidth / 10

        colorButtons.colorDrawables.forEachIndexed { index, colorDrawableId ->
            colorButtons.add(ImageView(this).apply {
                visibility = View.GONE
                isClickable = true
                setOnClickListener { colorButtons.setSelectedState(index) }
                setImageResource(colorDrawableId)
            }.apply {
                layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    circleConstraint = centerView.id
                    circleRadius = radius.toInt()
                    circleAngle =
                        if (colorButtons.colorType == ColorType.Pen)
                            180f - (7.5f + index * 15f)
                        else
                            180f + (7.5f + index * 15f)
                }
            })
        }

        colorButtons.forEach(rootView::addView)
    }

    private fun getCompatDrawable(resId: Int): Drawable {
        return ResourcesCompat.getDrawable(resources, resId, null)
            ?: throw Resources.NotFoundException("Null resource")
    }

    class ColorButtons(
        val colorType: ColorType,
        val colorDrawables: List<Int>,
    ) : ArrayList<ImageView>() {
        fun changeVisibility() {
            if (get(0).isShown) {
                setVisibility(View.GONE)
            } else {
                setVisibility(View.VISIBLE)
            }
        }

        fun resetVisibility() = setVisibility(View.GONE)

        fun setSelectedState(target: Int) {
            Log.d("Andy", "target = $target")
            forEachIndexed { index, imageView ->
                imageView.isSelected = target == index
            }
        }

        private fun setVisibility(visibility: Int) = forEach { it.visibility = visibility }
    }
}