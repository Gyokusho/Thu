package com.example.project;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.utils.RequestServer;
import com.example.utils.RequestServer.RequestResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SudokuPlayActivity extends Activity implements RequestResult {

	private class CellClickListener implements View.OnClickListener {
		private int row;
		private int col;
		
		public CellClickListener(int i, int j) {
			row = i;
			col = j;
		}

		@Override
		public void onClick(View v) {			
						
			if (q[row][col] == 0) {
				if (currentX != -1 && currentY != -1) {
					cells[currentX][currentY].setBackgroundResource(0);
				}
				cells[row][col].setBackgroundResource(R.drawable.border);
				currentX = row;
				currentY = col;
			} else {
				currentX = -1;
				currentY = -1;
			}
			
			
			
		}
	}
	
	
	private class ButtonNumberClickListener implements View.OnClickListener {

		private int number;
		
		public ButtonNumberClickListener(int n) {
			number = n;
		}
		
		@Override
		public void onClick(View v) {
			if (currentX != -1 && currentY != -1) {
				cells[currentX][currentY].setText("" +  number);
			}
			
		}
		
	}
	
	RequestServer rs;
	ProgressDialog pdial;
	boolean isPlaying = false;
	int[][] q = new int[10][10];
	TextView[][] cells = new TextView[10][10];
	TextView txtName;
	AbsoluteLayout tbl;
	int currentX = -1;
	int currentY = -1;
	float top;
	float left;
	
	private int WIDTH;
	private int HEIGHT;
	private AbsoluteLayout layout;
	private ImageView grid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku_play);
		
		txtName = (TextView) findViewById(R.id.txtName);
		grid = (ImageView) findViewById(R.id.grid);
		tbl = (AbsoluteLayout)  findViewById(R.id.table);								
		setUpView();
		if (!isPlaying) {
			setUpQuestion();
			isPlaying = true;
		}
					
	}
	
	private void setUpQuestion() {
		 for (int i = 1; i <= 9; i++) {
			 for (int j = 1; j <= 9; j++) {
				 q[i][j] = 0;
				 cells[i][j].setText("");				 				 
			 }
		 }
		
		 Map<String, String> query = new HashMap<String, String>();
		 query.put("action", "GetQuestion");
		 query.put("level", "1");
		 makeQuery(query, "SudokuServlet");
		 
		 
	}

	
	private void setUpView() {
		WIDTH = grid.getLayoutParams().width / 9 + 1;
		HEIGHT = grid.getLayoutParams().height / 9 ;
		layout = (AbsoluteLayout) findViewById(R.id.playView);
				
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j<= 9; j++) {
				TextView txt  = new TextView(this);
				
				txt.setLayoutParams(new LayoutParams(WIDTH, HEIGHT));				
				txt.setOnClickListener(new CellClickListener(i, j));
				txt.setGravity(android.view.Gravity.CENTER);
				
				txt.setTranslationX(WIDTH*(j-1));								
				txt.setTranslationY(HEIGHT*(i-1));				
				
				//txt.setBackgroundColor(R.drawable.cl_gold);
				txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);										
				
				tbl.addView(txt);								
				cells[i][j] = txt;
			}
		}
		
		LinearLayout l = (LinearLayout) findViewById(R.id.layoutBtn1);
		
		for (int i = 1; i <= 5; i++) {
			Button btn = (Button) l.getChildAt(i-1);
			btn.setOnClickListener(new ButtonNumberClickListener(i));
		}
		
		l = (LinearLayout) findViewById(R.id.layoutBtn2);
		
		for (int i = 6, j = 0; i <= 9; i++, j++) {
			Button btn = (Button) l.getChildAt(j);
			btn.setOnClickListener(new ButtonNumberClickListener(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sudoku_play, menu);
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
	
	private void makeQuery(Map<String, String> query, String path){
        pdial = new ProgressDialog(this);
        pdial.setMessage("Loading...");
        pdial.setTitle("Download Sudoku Question from Server");
        pdial.show();
		
		rs = new RequestServer();
        rs.delegate = this;
        rs.execute(query, path);               
    }

	@Override
	public void processFinish(String result) {		
		try {
			JSONObject jsonObj = new JSONObject(result);
			
			JSONArray q = jsonObj.getJSONArray("q");
			
			for (int i = 0; i < q.length(); i++) {
				JSONArray row = q.getJSONArray(i);
				for (int j = 0; j < row.length(); j++) {
					int num = row.getInt(j);
					this.q[i][j] = num;
					if (num != 0) {
						this.cells[i][j].setText("" + num);
					} else {
						if (i > 0 && j > 0) {
							
							this.cells[i][j].setTextColor(0x00008B);		
							this.cells[i][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
						}
							
					}
				}
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();	
		}
		
		pdial.dismiss();
			
	}

}
