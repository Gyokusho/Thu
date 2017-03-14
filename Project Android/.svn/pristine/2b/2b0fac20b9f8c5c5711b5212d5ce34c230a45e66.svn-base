package com.example.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;

import com.example.utils.RequestServer;
import com.example.utils.RequestServer.RequestResult;

import android.app.Activity;
import android.os.AsyncTask;
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
			if (currentX != -1 && currentY != -1) {
				cells[currentX][currentY].setBackgroundResource(0);
			}
			
			cells[row][col].setBackgroundResource(R.drawable.border);
			
			currentX = row;
			currentY = col;
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
	int[][] question = new int[10][10];
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
		setUpQuestion();			
	}
	
	private void setUpQuestion() {
		 for (int i = 1; i <= 9; i++) {
			 for (int j = 1; j <= 9; j++) {
				 question[i][j] = 0;
				 cells[i][j].setText("");
			 }
		 }
		
		 makeQuery("", "http://192.168.1.5:8084/Android/SudokuServlet");
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
	
	private void makeQuery(String query, String url){
        rs = new RequestServer();
        rs.delegate = this;
        rs.execute(query, url);
    }

	@Override
	public void processFinish(String result) {
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();		
	}
}
