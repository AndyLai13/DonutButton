package com.andylai.donutbuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DonutButtonsView view = findViewById(R.id.circularView);
		view.setOnClickListener(v -> Log.d("Andy", "onclick"));
	}
}