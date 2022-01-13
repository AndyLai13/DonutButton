package com.andylai.donutbuttons

import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.andylai.donutbuttons.ColorPicker.ColorType
import java.util.ArrayList

class CircleAnnotationBar(view: View) {
    private val openButton = view.findViewById<ImageView>(R.id.moveBtn)
        .apply { isClickable = true }
        .apply { setOnClickListener { setOpen(true) } }
        .apply {
            setOnTouchListener(object : View.OnTouchListener {
                var isMoving = false
                var viewLocation: PointF = PointF(0f, 0f)
                var initialPoint: PointF = PointF(0f, 0f)
                override fun onTouch(v: View, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            viewLocation.set(view.x, view.y)
                            initialPoint.set(event.rawX, event.rawY)
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val updateX = viewLocation.x + (event.rawX - initialPoint.x)
                            val updateY = viewLocation.y + (event.rawY - initialPoint.y)
                            isMoving = updateX > 0.01 && updateY > 0.01
                            if (isMoving) {
                                view.x = updateX
                                view.y = updateY
                                return true
                            }
//                        callback?.onLocationChange()
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!isMoving)
                                performClick()
                            isMoving = false
                            return true
                        }
                    }
                    return false
                }
            })
        }
    private val donutButtonsView = view.findViewById<DonutButtonsView>(R.id.donut)
        .apply { setPressableButtons(listOf(0, 1, 2), 1) }
        .apply {
            callback = object : DonutButtonsView.Callback {
                override fun onCenterTouched() {
                    setOpen(false)
                    Log.d("Andy", "onCenterTouched")
                }

                override fun onArcButtonClick(index: Int) {
                    Log.d("Andy", "onArcButtonClick , index = $index")
//                    when (index) {
//                        3 -> this@CircleBar.callback?.onCloseClick()
//                        4 -> this@CircleBar.callback?.onDeleteClick()
//                        5 -> this@CircleBar.callback?.onUndoClick()
//                        6 -> this@CircleBar.callback?.onRedoClick()
//                        7 -> this@CircleBar.callback?.onSaveClick()
//                    }
                }

                override fun onArcButtonPressed(index: Int) {
                    Log.d("Andy", "onArcButtonPressed , index =  $index")
                    penButtons.resetVisibility()
                    brushButtons.resetVisibility()

//                    when (index) {
//                        0 -> this@CircleBar.callback?.onChangeMode(AnnotationBar.Mode.ERASER)
//                        1 -> this@CircleBar.callback?.onChangeMode(AnnotationBar.Mode.PEN)
//                        2 -> this@CircleBar.callback?.onChangeMode(AnnotationBar.Mode.HIGHLIGHTER)
//                    }
                }

                override fun onArcButtonClickUnderPressed(index: Int) {
                    Log.d("Andy", "onArcButtonClickUnderPressed , index = $index")
                    if (index == 1)
                        penButtons.changeVisibility()
                    else if (index == 2)
                        brushButtons.changeVisibility()
                }
            }
        }
    private val penButtons = ColorButtons(ColorType.Pen)
        .apply {
            add(view.findViewById(R.id.penBtn1))
            add(view.findViewById(R.id.penBtn2))
            add(view.findViewById(R.id.penBtn3))
            add(view.findViewById(R.id.penBtn4))
            add(view.findViewById(R.id.penBtn5))
            add(view.findViewById(R.id.penBtn6))
            add(view.findViewById(R.id.penBtn7))
            add(view.findViewById(R.id.penBtn8))
        }.apply {
            setSelectedState(this)
            resetVisibility()
        }

    private val brushButtons = ColorButtons(ColorType.HighLighter)
        .apply {
            add(view.findViewById(R.id.brushBtn1))
            add(view.findViewById(R.id.brushBtn2))
            add(view.findViewById(R.id.brushBtn3))
            add(view.findViewById(R.id.brushBtn4))
            add(view.findViewById(R.id.brushBtn5))
            add(view.findViewById(R.id.brushBtn6))
            add(view.findViewById(R.id.brushBtn7))
            add(view.findViewById(R.id.brushBtn8))
        }.apply {
            setSelectedState(this)
            resetVisibility()
        }

    private fun setSelectedState(colorButtons: ColorButtons) {
        colorButtons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                colorButtons.setSelectedState(index)
                donutButtonsView.setColorIconDrawable(colorButtons.colorType, index)
            }
        }
    }

    fun setOpen(isOpen: Boolean) {
        if (isOpen) {
            openButton.visibility = View.GONE
            donutButtonsView.visibility = View.VISIBLE
            penButtons.setVisibility(View.VISIBLE)
        } else {
            openButton.visibility = View.VISIBLE
            donutButtonsView.visibility = View.GONE
            penButtons.setVisibility(View.GONE)
        }
    }

    fun setPenVisibility(visibility: Int) {
        penButtons.forEach { it.visibility = visibility }
    }

    fun setBrushVisibility(visibility: Int) {
        brushButtons.forEach { it.visibility = visibility }
    }

    fun setUndoEnabled(undoable: Boolean) {
        donutButtonsView.setUndoEnabled(undoable)
    }

    fun setRedoEnabled(redoable: Boolean) {
        donutButtonsView.setRedoEnabled(redoable)
    }

    class ColorButtons(val colorType: ColorType) : ArrayList<ImageView>() {
        fun changeVisibility() {
            if (get(0).isShown) {
                setVisibility(View.GONE)
            } else {
                setVisibility(View.VISIBLE)
            }
        }

        fun setSelectedState(target: Int) {
            forEachIndexed { index, imageView -> imageView.isSelected = target == index }
        }

        fun resetVisibility() = setVisibility(View.GONE)
        fun setVisibility(visibility: Int) = forEach { it.visibility = visibility }
    }
}