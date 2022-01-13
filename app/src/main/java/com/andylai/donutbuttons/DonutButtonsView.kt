package com.andylai.donutbuttons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.andylai.donutbuttons.ColorPicker.ColorType
import com.andylai.donutbuttons.ColorPicker.RES_HIGHLIGHTER_COLORS_DRAWABLES
import com.andylai.donutbuttons.ColorPicker.RES_PEN_COLOR_DRAWABLES
import java.util.*
import kotlin.math.*

class DonutButtonsView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "DonutButtonsView"
        private const val COUNT = 8
    }

    private val arcButtons: MutableList<ArcButton> = ArrayList()
    private val pressablePool: MutableList<Int> = ArrayList()
    private val center = PointF()
    private val sweepAngle = 360f / COUNT
    private var circleRadius = 0f
    private var innerRadius = 0f
    private var outerRadius = 0f
    private var iconRadius = 0f
    private var circleButtonSize = 0f
    private var circularButtonSize = 0f
    private var pressedItem = -1
    private val colorPressed: Int
    private val colorNormal: Int
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    var callback: Callback? = null

    init {
        colorPressed = getCompatColor(R.color.btn_pressed)
        colorNormal = Color.WHITE
        initArcButtons()
        isClickable = true
        isFocusable = true
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun setUndoEnabled(undoable: Boolean) {
        arcButtons[5].enabled = undoable
        invalidate()
    }

    fun setRedoEnabled(redoable: Boolean) {
        arcButtons[6].enabled = redoable
        invalidate()
    }

    fun setPressableButtons(indices: List<Int>, default: Int) {
        pressablePool.addAll(indices)
        if (pressablePool.contains(default)) {
            pressedItem = default
            setArcBtnPressed(default)
        } else {
            Log.d(TAG, "$default is not pressable arc button.")
        }
    }

    fun setColorIconDrawable(colorType: ColorType, index: Int) {
        if (colorType == ColorType.Pen)
            setPenIconDrawable(index)
        else
            setHighlighterIconDrawable(index)
    }

    private fun setPenIconDrawable(index: Int) {
        arcButtons[1].drawable = getCompatDrawable(RES_PEN_COLOR_DRAWABLES[index])
        invalidate()
    }

    private fun setHighlighterIconDrawable(index: Int) {
        arcButtons[2].drawable = getCompatDrawable(RES_HIGHLIGHTER_COLORS_DRAWABLES[index])
        invalidate()
    }

    private fun initArcButtons() {
        arcButtons.add(ArcButton(context, getCompatDrawable(R.drawable.ic_donut_eraser)))
        arcButtons.add(ArcButton(context, getCompatDrawable(R.drawable.ic_donut_pen_picker5)))
        arcButtons.add(ArcButton(context, getCompatDrawable(R.drawable.ic_donut_brush_picker9)))
        arcButtons.add(ArcButton(context, getCompatDrawable(R.drawable.ic_donut_close)))
        arcButtons.add(ArcButton(context, getCompatDrawable(R.drawable.ic_donut_delete)))
        arcButtons.add(
            ArcButton(
                context = context,
                drawable = getCompatDrawable(R.drawable.ic_donut_undo),
                disabledDrawable = getCompatDrawable(R.drawable.ic_donut_undo_disabled),
            )
        )
        arcButtons.add(
            ArcButton(
                context = context,
                drawable = getCompatDrawable(R.drawable.ic_donut_redo),
                disabledDrawable = getCompatDrawable(R.drawable.ic_donut_redo_disabled),
            )
        )
        arcButtons.add(ArcButton(context, getCompatDrawable(R.drawable.ic_donut_save)))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        center[width / 2f] = height / 2f
        innerRadius = width / 6f
        outerRadius = width / 2f
        circleRadius = width / 8f
        iconRadius = width / 3f
        circleButtonSize = width / 12f
        circularButtonSize = width / 4f
        drawArcs(canvas)
        drawIcons(canvas)
        drawCentralCircular(canvas)
        drawCentralCircle(canvas)
    }

    private fun drawArcs(canvas: Canvas) {
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        for (i in 0 until COUNT) {
            val startAngle = sweepAngle * i
            paint.color = arcButtons[i].color
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint)
        }
    }

    private fun drawIcons(canvas: Canvas) {
        val firstAngle = sweepAngle / 2
        for (i in 0 until COUNT) {
            val angle = firstAngle + sweepAngle * i
            val iconRect = getIconRect(iconRadius, circularButtonSize, center, angle)
            val drawableIcon = if (arcButtons[i].enabled) {
                arcButtons[i].drawable
            } else {
                arcButtons[i].disabledDrawable ?: arcButtons[i].drawable
            }
            drawableIcon.bounds = iconRect
            drawableIcon.draw(canvas)
        }
    }

    private fun drawCentralCircular(canvas: Canvas) {
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawCircle(center.x, center.y, innerRadius, paint)
        paint.xfermode = null
    }

    private fun drawCentralCircle(canvas: Canvas) {
        paint.color = colorNormal
        canvas.drawCircle(center.x, center.y, circleRadius, paint)
        val drawableIcon = getCompatDrawable(R.drawable.ic_donut_move)
        Log.d("Andy", "${drawableIcon.intrinsicWidth}")
        Log.d("Andy", "${drawableIcon.minimumWidth}")
        Log.d("Andy", "${circleButtonSize}")
        drawableIcon.bounds = Rect(
            (center.x - circleButtonSize).toInt(),
            (center.y - circleButtonSize).toInt(),
            (center.x + circleButtonSize).toInt(),
            (center.y + circleButtonSize).toInt()
        )
        drawableIcon.draw(canvas)
    }

    private fun getIconRect(
        iconRadius: Float,
        desiredIconSize: Float,
        center: PointF,
        angle: Float
    ): Rect {
        val angleRadian = Math.toRadians(angle.toDouble())
        val rCos = iconRadius * cos(angleRadian)
        val rSin = iconRadius * sin(angleRadian)
        var l = rCos - desiredIconSize / 2
        var t = rSin - desiredIconSize / 2
        var r = rCos + desiredIconSize / 2
        var b = rSin + desiredIconSize / 2

        // translate to center
        l += center.x.toDouble()
        t += center.y.toDouble()
        r += center.x.toDouble()
        b += center.y.toDouble()
        return Rect(l.toInt(), t.toInt(), r.toInt(), b.toInt())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val center = PointF(width / 2f, height / 2f)
        val translateX = x - center.x
        val translateY = y - center.y
        val distance = sqrt(translateX.toDouble().pow(2.0) + translateY.toDouble().pow(2.0))
        val degree = getDegree(translateX.toDouble(), translateY.toDouble())
        return when {
            distance < circleRadius -> onTouchCenterButton(event)
            circleRadius < distance && distance < innerRadius -> onTouchTransparentArea(event)
            innerRadius < distance && distance < outerRadius -> onTouchArcButton(event, degree)
            else -> onTouchUndefinedArea(event)
        }
    }

    private fun onTouchCenterButton(event: MotionEvent): Boolean {
        callback?.onCenterTouched()
        return super.onTouchEvent(event)
    }

    private fun onTouchTransparentArea(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    private fun onTouchArcButton(event: MotionEvent, degree: Double): Boolean {
        val index = getIndex(degree)
        if (arcButtons[index].enabled) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                setArcBtnPressed(index)
                return true
            } else if (event.action == MotionEvent.ACTION_UP) {
                val isPressable = pressablePool.contains(index)
                if (isPressable) {
                    if (pressedItem == index) {
                        callback?.onArcButtonClickUnderPressed(index)
                    } else {
                        pressedItem = index
                        callback?.onArcButtonPressed(index)
                    }
                } else {
                    pressedItem = -1
                    callback?.onArcButtonClick(index)
                    setArcBtnPressed(-1)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun setArcBtnPressed(index: Int) {
        for (i in 0 until COUNT) {
            if (i == index)
                arcButtons[i].color = colorPressed
            else
                arcButtons[i].color = colorNormal
        }
        invalidate()
    }

    private fun onTouchUndefinedArea(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    private fun getDegree(x: Double, y: Double): Double {
        val degree = Math.toDegrees(atan2(y, x))
        return if (degree > 0) degree else 360 + degree
    }

    /**
     * Index range from (index - 1) to index, would be 1 for segment 0~1 and 4 for segment 3~4,
     * therefore return value should be index-1
     */
    private fun getIndex(degree: Double): Int {
        var index = 0
        while (sweepAngle * index < degree) {
            index++
        }
        return index - 1
    }

    private fun getCompatDrawable(resId: Int): Drawable = Utils.getCompatDrawable(resources, resId)
    private fun getCompatColor(resId: Int): Int = Utils.getCompatColor(resources, resId)

    interface Callback {
        fun onCenterTouched()
        fun onArcButtonClick(index: Int)
        fun onArcButtonPressed(index: Int)
        fun onArcButtonClickUnderPressed(index: Int)
    }

    class ArcButton(
        context: Context,
        var drawable: Drawable = Utils.getCompatDrawable(
            context.resources,
            R.drawable.ic_donut_pen
        ),
        var disabledDrawable: Drawable? = null,
        var color: Int = Color.WHITE,
        var enabled: Boolean = true
    )
}