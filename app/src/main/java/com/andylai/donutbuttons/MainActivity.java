package com.andylai.donutbuttons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DonutButtonsView view = findViewById(R.id.donutButtonView);
		view.setCallback(new DonutButtonsView.Callback() {
			@Override
			public void onCenterTouched() {
				Log.d("Andy", "onCenterTouched");
			}

			@Override
			public void onCircularButtonTouched(int index) {
				Log.d("Andy", "onCircularButtonTouched " + index);
			}
		});
		List<Drawable> icons = new ArrayList<>();
		icons.add(getCompatDrawable(R.drawable.ic_donut_eraser));
		icons.add(getCompatDrawable(R.drawable.ic_donut_pen));
		icons.add(getCompatDrawable(R.drawable.ic_donut_brush));
		icons.add(getCompatDrawable(R.drawable.ic_donut_close));
		icons.add(getCompatDrawable(R.drawable.ic_donut_delete));
		icons.add(getCompatDrawable(R.drawable.ic_donut_undo));
		icons.add(getCompatDrawable(R.drawable.ic_donut_redo));
		icons.add(getCompatDrawable(R.drawable.ic_donut_save));
		view.setIconDrawables(icons);
	}

	private Drawable getCompatDrawable(int resId) {
		return ResourcesCompat.getDrawable(getResources(), resId, null);
	}
}