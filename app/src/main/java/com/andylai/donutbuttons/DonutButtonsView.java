package com.andylai.donutbuttons;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

public class DonutButtonsView extends View {
	private final static int COUNT = 8;
	private final static int COLOR_PRESSED = Color.RED;
	private final static int COLOR_NORMAL = Color.WHITE;
	private final List<ArcButton> arcButtons = new ArrayList<>();
	private final Paint paint = new Paint();
	private final PointF center = new PointF();
	private final float sweepAngle = 360f / COUNT;

	private float circleRadius;
	private float innerRadius;
	private float outerRadius;
	private float iconRadius;
	private float circleButtonSize;
	private float circularButtonSize;
	private Callback callback;

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
		initArcButtons();
		setClickable(true);
		setFocusable(true);
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void setDefaultIndex(int index) {
		if (index >= COUNT)
			throw new IndexOutOfBoundsException("Index should be smaller than count!");
		arcButtons.get(index).setColor(COLOR_PRESSED);
	}

	public void setIconDrawables(List<Drawable> drawables) {
		if (drawables.size() != arcButtons.size())
			throw new IndexOutOfBoundsException("Size is not matched");
		for (int i = 0; i < COUNT; i++)
			arcButtons.get(i).setDrawable(drawables.get(i));
	}

	public void setPressed(int index) {
		setArcBtnPressed(index);
		invalidate();
	}

	private void initArcButtons() {
		for (int i = 0; i < COUNT; i++)
			arcButtons.add(new ArcButton(getContext()));
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
			paint.setColor(arcButtons.get(i).getColor());
			canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
		}
	}

	private void drawIcons(Canvas canvas) {
		float firstAngle = sweepAngle / 2;
		for (int i = 0; i < COUNT; i++) {
			float angle = firstAngle + sweepAngle * i;
			Rect iconRect = getIconRect(iconRadius, circularButtonSize, center, angle);
			Drawable drawableIcon = arcButtons.get(i).getDrawable();
			drawableIcon.setBounds(iconRect);
			drawableIcon.draw(canvas);
		}
	}

	private void drawCentralCircular(Canvas canvas) {
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		canvas.drawCircle(center.x, center.y, innerRadius, paint);
		paint.setXfermode(null);
	}

	private void drawCentralCircle(Canvas canvas) {
		paint.setColor(COLOR_NORMAL);
		canvas.drawCircle(center.x, center.y, circleRadius, paint);
		Drawable drawableIcon = getCompatDrawable(R.drawable.ic_donut_move);
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

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		PointF center = new PointF(getWidth() / 2f, getHeight() / 2f);
		float translateX = x - center.x;
		float translateY = y - center.y;
		double distance = Math.sqrt(Math.pow(translateX, 2) + Math.pow(translateY, 2));
		double degree = getDegree(translateX, translateY);

		if (distance < circleRadius) {
			return onTouchCenterButton(event);
		} else if (circleRadius < distance && distance < innerRadius) {
			return onTouchCircularTransparentArea(event);
		} else if (innerRadius < distance && distance < outerRadius) {
			return onTouchCircularButton(event, degree);
		} else {
			return onTouchUndefinedArea(event);
		}
	}

	private boolean onTouchCenterButton(MotionEvent event) {
		if (callback != null)
			callback.onCenterTouched();
		Log.d("Andy", "onTouchCenterButton");
		return onTouchEvent(event);
	}

	private boolean onTouchCircularTransparentArea(MotionEvent event) {
		Log.d("Andy", "onTouchCircularTransparentArea");
		return super.onTouchEvent(event);
	}

	private boolean onTouchCircularButton(MotionEvent event, double degree) {
		int index = getIndex(degree);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			setArcBtnPressed(index);
			invalidate();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			setArcBtnPressed(-1);
			if (callback != null)
				callback.onCircularButtonTouched(this, index);
			invalidate();
			return true;
		}
		return super.onTouchEvent(event);
	}

	private void setArcBtnPressed(int index) {
		for (int i = 0; i < COUNT; i++) {
			if (i == index)
				arcButtons.get(i).setColor(COLOR_PRESSED);
			else
				arcButtons.get(i).setColor(COLOR_NORMAL);
		}
	}

	private boolean onTouchUndefinedArea(MotionEvent event) {
		Log.d("Andy", "onTouchUndefinedArea");
		return super.onTouchEvent(event);
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
		return Utils.getCompatDrawable(getResources(), resId);
	}

	public interface Callback {
		void onCenterTouched();

		void onCircularButtonTouched(DonutButtonsView self, int index);
	}

	private static class ArcButton {
		private int color;
		private Drawable drawable;

		public ArcButton(Context context) {
			this.color = COLOR_NORMAL;
			this.drawable = Utils.getCompatDrawable(context.getResources(), R.drawable.ic_donut_pen);
		}

		public int getColor() {
			return color;
		}

		public Drawable getDrawable() {
			return drawable;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public void setDrawable(Drawable drawable) {
			this.drawable = drawable;
		}
	}
}
