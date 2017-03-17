package com.example.project;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.utils.RequestServer;
import com.example.utils.RequestServer.RequestResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
	
	RequestServer rs;	
	int cl_darkblue = Color.parseColor("#1818df");
	int cl_redpink = Color.parseColor("#ff4c4c");
	
	ProgressDialog pdial;
	boolean isPlaying = false;
	int[][] q = new int[10][10];	
	TextView[][] cells = new TextView[10][10];
	TextView txtName;
	TextView txtTurn;
	AbsoluteLayout tbl;
	int currentX = -1;
	int currentY = -1;
	float top;
	float left;
	int level;
	int turn;
	
	private int WIDTH;
	private int HEIGHT;
	private AbsoluteLayout layout;
	private ImageView grid;	
	

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
				
				turn++;
				
				txtTurn.setText("" + turn);
				
				cells[currentX][currentY].setText("" +  number);
				cells[currentX][currentY].setTextColor(cl_darkblue);
				checkDuplicate();				
				if (isWin()) {
					finishGame();
				}
			}
						
		}
		
		private boolean checkDuplicate() {
			
			boolean isDuplicated = false;
			
			for (int i = 1; i <= 9; i++) {
				int num_same_col = Integer.parseInt("0" + cells[currentX][i].getText().toString());
				int num_same_row = Integer.parseInt("0" + cells[i][currentY].getText().toString());
				
				if (i != currentY) {
					if (num_same_col == this.number) {
						isDuplicated = true;
						cells[currentX][i].setTextColor(cl_redpink);
						cells[currentX][currentY].setTextColor(cl_redpink);
					} else {					
						cells[currentX][i].setTextColor((q[currentX][i] == 0)?cl_darkblue:Color.DKGRAY);
					}
				}
				
				if (i != currentX) {
					if (num_same_row == this.number) {
						isDuplicated = true;
						cells[i][currentY].setTextColor(cl_redpink);
						cells[currentX][currentY].setTextColor(cl_redpink);
					} else {					
						cells[i][currentY].setTextColor((q[i][currentY] == 0)?cl_darkblue:Color.DKGRAY);
					}
				}				
			}
			
			return isDuplicated;
		}
		
		private boolean isWin() {
			boolean isWin = true;									
					
			for (int i=1 ; i <= 9; i++) {
				for (int j=1; j<=9; j++) {
					 String cell = cells[i][j].getText().toString().trim();
					 if (cell.isEmpty()) {
						 isWin = false;						 						 
					 } else {
						 
						 for (int k = 1; k <= 9; k++) {							 
							 if (k != j && cells[i][k].getText().toString().trim().equals(cell)) {
								 isWin = false;	
							 }
							 
							 if (k != i && cells[k][j].getText().toString().trim().equals(cell)) {
								 isWin = false;
							 }
						 }
						 
					 }
				}
			}
									
			return isWin;
		}
	
		private void finishGame() {							
			
			
			AlertDialog alert = new AlertDialog.Builder(SudokuPlayActivity.this)
				.setTitle("Congratulation!")
				.setMessage("You solve the Sudoku after " + turn + " moves")
				.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setUpQuestion();
					}
				}).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//finishActivity();
						SudokuPlayActivity.this.finish();
					}
				})
				.create();
			alert.show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku_play);
		
		txtName = (TextView) findViewById(R.id.txtName);
		txtTurn = (TextView) findViewById(R.id.sudokuPlay_txtTurn);
		grid = (ImageView) findViewById(R.id.grid);
		tbl = (AbsoluteLayout)  findViewById(R.id.table);
		
		Bundle bundle = getIntent().getBundleExtra("bundle");
		txtName.setText(bundle.getString("name"));
		level = bundle.getInt("level");
		
		setUpView();
		if (!isPlaying) {
			setUpQuestion();
			isPlaying = true;
		}
					
	}
	
	private void finishActivity() {
		this.finish();
	}
	
	private void setUpQuestion() {
		turn = 0;
		
		txtTurn.setText("" + turn);
		
		 for (int i = 1; i <= 9; i++) {
			 for (int j = 1; j <= 9; j++) {
				 q[i][j] = 0;
				 cells[i][j].setText("");				 				 
			 }
		 }
		
		 Map<String, String> query = new HashMap<String, String>();
		 query.put("action", "GetQuestion");
		 query.put("level", "" + level);
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
				//txt.setTextColor(Color.DKGRAY);
				
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
        pdial.setTitle("Get Sudoku Question");
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
						this.cells[i][j].setTextColor(Color.DKGRAY);						
					} else {
						if (i > 0 && j > 0) {
							
							this.cells[i][j].setTextColor(cl_darkblue);		
							this.cells[i][j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
						}
							
					}
				}
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();	
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		
		pdial.dismiss();			
	}
	
	public void newGame(View v) {
		//setUpView();
		setUpQuestion();
	}

}
