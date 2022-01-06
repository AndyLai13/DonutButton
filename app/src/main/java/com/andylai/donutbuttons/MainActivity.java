package com.andylai.donutbuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
	}
}