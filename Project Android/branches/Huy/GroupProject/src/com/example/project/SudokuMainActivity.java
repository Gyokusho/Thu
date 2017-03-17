package com.example.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SudokuMainActivity extends Activity {

	private SeekBar sbLevel;
	private TextView txtName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku_main);
	
		txtName = (TextView) findViewById(R.id.sudokuMain_txtName);
		sbLevel = (SeekBar) findViewById(R.id.sbLevel);
		sbLevel.setMax(2);				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sudoku_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void playSudoku(View v) {
		String name = txtName.getText().toString().trim();
		
		if (name.isEmpty()) {
			new AlertDialog.Builder(this).setTitle("Please input your name").create().show();
		} else {
			Bundle b = new Bundle();
			b.putInt("level", sbLevel.getProgress() + 1);
			b.putString("name", name);
			
			Intent i = new Intent(this, SudokuPlayActivity.class);
			i.putExtra("bundle", b);
			startActivity(i);
		}
		
		
		
	}
}
