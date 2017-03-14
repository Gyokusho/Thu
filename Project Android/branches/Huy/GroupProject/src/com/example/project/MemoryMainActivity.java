package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MemoryMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_main);
	}

	public void clickToMediumGame(View view) {
		Intent intent = new Intent(this, MemoryPlayActivity.class);
		intent.putExtra("memoryLevel", "Medium");
		startActivity(intent);
	}

	public void clickToHardGame(View view) {
		Intent intent = new Intent(this, MemoryPlayActivity.class);
		intent.putExtra("memoryLevel", "Hard");
		startActivity(intent);
	}
}
