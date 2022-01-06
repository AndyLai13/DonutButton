package com.andylai.donutbuttons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class DonutButtonsView extends View {
	private final static int COUNT = 8;
	private final static int COLOR_PRESSED = Color.RED;
	private final static int COLOR_NORMAL = Color.WHITE;
	private final List<Integer> colors = new ArrayList<>();
	private final List<Drawable> icons = new ArrayList<>();
	private final Paint paint = new Paint();
	private final PointF center = new PointF();
	private final float sweepAngle = 360f / COUNT;

	private float circleRadius;
	private float innerRadius;
	private float outerRadius;
	private float iconRadius;
	private float circleButtonSize;
	private float circularButtonSize;

	public DonutButtonsView(Context context) {
		this(context, null);
	}

	public DonutButtonsView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DonutButtonsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		init();
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

	private void init() {
		fillWithNormalColor();
		fillWithDefaultIcons();
		setDefaultIndex(0);
		setClickable(true);
		setFocusable(true);
	}

	private void fillWithNormalColor() {
		for (int i = 0; i < COUNT; i++)
			colors.add(COLOR_NORMAL);
	}

	public void setDefaultIndex(int index) {
		if (index >= COUNT)
			throw new IndexOutOfBoundsException("Index should be smaller than count!");
		colors.set(index, COLOR_PRESSED);
	}

	private void fillWithDefaultIcons() {
		icons.add(getCompatDrawable(R.drawable.ic_anno_eraser_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_pen_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_brush_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_close_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_delete_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_undo_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_redo_white));
		icons.add(getCompatDrawable(R.drawable.ic_anno_save_white));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		center.set(getWidth() / 2f, getHeight() / 2f);
		innerRadius = getWidth() / 6f;
		outerRadius = getWidth() / 2f;
		circleRadius = getWidth() / 8f;
		iconRadius = getWidth() / 3f;
		circleButtonSize = getWidth() / 12f;
		circularButtonSize = getWidth() / 4f;

		drawArcs(canvas);
		drawIcons(canvas);
		drawCentralCircular(canvas);
		drawCentralCircle(canvas);
	}

	private void drawArcs(Canvas canvas) {
		RectF rect = new RectF(0, 0, getWidth(), getHeight());
		for (int i = 0; i < COUNT; i++) {
			float startAngle = sweepAngle * i;
			paint.setColor(colors.get(i));
			canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
		}
	}

	private void drawIcons(Canvas canvas) {
		float firstAngle = sweepAngle / 2;
		for (int i = 0; i < colors.size(); i++) {
			float angle = firstAngle + sweepAngle * i;
			Rect iconRect = getIconRect(iconRadius, circularButtonSize, center, angle);
			Drawable drawableIcon = getIcon(i);
			drawableIcon.setBounds(iconRect);
			drawableIcon.draw(canvas);
		}
	}

	private Drawable getIcon(int index) {
		if (icons.isEmpty())
			return getCompatDrawable(R.drawable.ic_anno_pen_green);
		else
			return icons.get(index);
	}

	private void drawCentralCircular(Canvas canvas) {
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		canvas.drawCircle(center.x, center.y, innerRadius, paint);
		paint.setXfermode(null);
	}

	private void drawCentralCircle(Canvas canvas) {
		paint.setColor(COLOR_NORMAL);
		canvas.drawCircle(center.x, center.y, circleRadius, paint);
		Drawable drawableIcon = getCompatDrawable(R.drawable.ic_eva_move_fill);
		drawableIcon.setBounds(new Rect(
				(int) (center.x - circleButtonSize),
				(int) (center.y - circleButtonSize),
				(int) (center.x + circleButtonSize),
				(int) (center.y + circleButtonSize)));
		drawableIcon.draw(canvas);
	}

	private Rect getIconRect(float iconRadius, float desiredIconSize, PointF center, float angle) {
		double angleRadian = Math.toRadians(angle);
		double r_cos = iconRadius * Math.cos(angleRadian);
		double r_sin = iconRadius * Math.sin(angleRadian);

		double l = (r_cos - desiredIconSize / 2);
		double t = (r_sin - desiredIconSize / 2);
		double r = (r_cos + desiredIconSize / 2);
		double b = (r_sin + desiredIconSize / 2);

		// translate to center
		l += center.x;
		t += center.y;
		r += center.x;
		b += center.y;
		return new Rect((int) l, (int) t, (int) r, (int) b);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			PointF center = new PointF(getWidth() / 2f, getHeight() / 2f);
			float translateX = x - center.x;
			float translateY = y - center.y;
			double distance = Math.sqrt(Math.pow(translateX, 2) + Math.pow(translateY, 2));
			if (distance < circleRadius) {
				onTouchCenterButton();
			} else if (distance > innerRadius) {
				onTouchCircularButton(translateX, translateY);
			} else {
				onTouchCircularTransparentArea();
			}
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	private void onTouchCenterButton() {
		Log.d("Andy", "in center");
	}

	private void onTouchCircularButton(float x, float y) {
		double degree = getDegree(x, y);
		int index = getIndex(degree);
		for (int i = 0; i < colors.size(); i++) {
			if (i == index)
				colors.set(i, COLOR_PRESSED);
			else
				colors.set(i, COLOR_NORMAL);
		}
	}

	private void onTouchCircularTransparentArea() {
		Log.d("Andy", "on transparent area");
	}

	private double getDegree(double x, double y) {
		double degree = Math.toDegrees(Math.atan2(y, x));
		return degree > 0 ? degree : 360 + degree;
	}

	/**
	 * Index range from (index - 1) to index, would be 1 for segment 0~1 and 4 for segment 3~4,
	 * therefore return value should be index-1
	 */
	private int getIndex(double degree) {
		int index = 0;
		while (sweepAngle * index < degree) {
			index++;
		}
		return index - 1;
	}

	private Drawable getCompatDrawable(int resId) {
		return ResourcesCompat.getDrawable(getResources(), resId, null);
	}
}
