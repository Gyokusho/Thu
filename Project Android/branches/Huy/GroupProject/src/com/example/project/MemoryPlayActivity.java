package com.example.project;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.custom.MemoryButton;
import com.example.project.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemoryPlayActivity extends Activity implements OnClickListener {
	private String gameLevel;
	private int numOfElements;
	private MemoryButton[] buttonArray;

	private int[] buttonGraphicLocations;
	private int[] buttonGraphics;
	private int numOfTurn = 0;

	private int numOfMatchedPair = 0;
	private int gameScore = 0;

	private MemoryButton selectedButton1;
	private MemoryButton selectedButton2;

	private TextView txtTurnInfo;

	private boolean isBusy = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_play);

		GridLayout memoryGameLayout = (GridLayout) findViewById(id.memoryGameLayout);
		LinearLayout parentGridLayout = (LinearLayout) findViewById(id.parentGridLayout);
		LinearLayout gameInfoLayout = (LinearLayout) findViewById(id.gameInfoLayout);
		txtTurnInfo = (TextView) findViewById(id.txtTurn);

		// set layout size
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int screenWidth = displayMetrics.widthPixels;
		int screenHeight = displayMetrics.heightPixels;
		if (screenWidth > screenHeight) {
			parentGridLayout.setMinimumWidth(screenWidth * (2 / 3));
			gameInfoLayout.setMinimumWidth(screenWidth * (1 / 3) - 10);
		} else {
			parentGridLayout.setMinimumWidth(screenHeight * (2 / 3));
			gameInfoLayout.setMinimumWidth(screenHeight * (1 / 3) - 10);
		}

		// get Level of game
		Intent intent = getIntent();
		gameLevel = intent.getStringExtra("memoryLevel");
		TextView txtLevel = (TextView) findViewById(id.txtLevel);
		txtLevel.setText(gameLevel);

		if (gameLevel.equalsIgnoreCase("medium")) {
			memoryGameLayout.setColumnCount(4);
			memoryGameLayout.setRowCount(4);
			numOfElements = 16;
		} else {
			memoryGameLayout.setColumnCount(6);
			memoryGameLayout.setRowCount(6);
			numOfElements = 36;
		}
		txtTurnInfo.setText(numOfTurn + " turns");

		buttonArray = new MemoryButton[numOfElements];
		buttonGraphics = new int[numOfElements / 2];
		buttonGraphicLocations = new int[numOfElements];

		loadButtonGraphics(gameLevel);

		int row = 0;
		int column = 0;
		int numColumn = memoryGameLayout.getColumnCount();
		int numRow = memoryGameLayout.getRowCount();
		for (row = 0; row < numRow; row++) {
			for (column = 0; column < numColumn; column++) {
				MemoryButton tempButton = new MemoryButton(this, row, column,
						buttonGraphics[buttonGraphicLocations[row * numColumn + column]], R.drawable.question);
				tempButton.setId(generateViewId());
				tempButton.setOnClickListener(this);
				buttonArray[row * numColumn + column] = tempButton;
				memoryGameLayout.addView(tempButton);
			}
		}
	}

	// do View.generateID chi ho tro api 17 tro len nen phai su dung ham nay
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	
	public static int generateViewId() {
		for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
	}
	
//	public int getHighScore(){
//		int highscore = 0;
//		try {
//			String filepath = "src/com/example/xml/MemoryGame.xml";
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();			
//			Document doc = docBuilder.parse(filepath);
//			NodeList accountList = doc.getElementsByTagName("account");
//			Node nNode = accountList.item(0);			
//			Element element = (Element) nNode;
//			highscore = Integer.parseInt(element.getElementsByTagName("highscore").item(0).getTextContent());			
//		} catch (ParserConfigurationException pce) {
//			pce.printStackTrace(); 
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		} catch (SAXException sae) {
//			sae.printStackTrace();
//		}
//		return highscore;
//	}
	
//	public static void updateXmlFIle(int newHighscore) {
//		try {
//			String filepath = "src/com/example/xml/MemoryGame.xml";
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();			
//			Document doc = docBuilder.parse(filepath);
//			NodeList accountList = doc.getElementsByTagName("account");
//			Node nNode = accountList.item(0);			
//			Element element = (Element) nNode;
//			element.getElementsByTagName("highscore").item(0).setTextContent(String.valueOf(newHighscore));			
//			// write the content into xml file
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			StreamResult result = new StreamResult(new File(filepath));
//			transformer.transform(source, result);			
//		} catch (ParserConfigurationException pce) {
//			pce.printStackTrace(); 
//		} catch (TransformerException tfe) {
//			tfe.printStackTrace();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		} catch (SAXException sae) {
//			sae.printStackTrace();
//		}
//
//	}
	
	public int getHighscore(){
		String s = "";
		int highscore = 0;
		try{
			FileInputStream fIn = openFileInput("highscore.txt");
			InputStreamReader isr = new InputStreamReader(fIn);
			char[] inputBuffer  = new char[15];		
			int charRead;
			while((charRead=isr.read(inputBuffer))>0){
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				s += readString;
				inputBuffer = new char[15];
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		if(!s.toString().trim().equalsIgnoreCase("") || null==s){
			//set chuoi o txtTurnInfo de check gia tri
			txtTurnInfo.setText(s);
			
			// parse bi loi
			//highscore = Integer.parseInt(s.toString().trim());
		}
		return highscore;
	}
	
	public void updateHighScore(int newHighscore){
		try{
			FileOutputStream fOut = openFileOutput("highscore.txt", MODE_WORLD_READABLE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(newHighscore);
			osw.flush();
			osw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public void loadButtonGraphics(String gameLevel) {
		buttonGraphics[0] = R.drawable.item1;
		buttonGraphics[1] = R.drawable.item2;
		buttonGraphics[2] = R.drawable.item3;
		buttonGraphics[3] = R.drawable.item4;
		buttonGraphics[4] = R.drawable.item5;
		buttonGraphics[5] = R.drawable.item6;

		buttonGraphics[6] = R.drawable.item7;
		buttonGraphics[7] = R.drawable.item8;
		if (!gameLevel.equalsIgnoreCase("medium")) {
			buttonGraphics[8] = R.drawable.item9;
			buttonGraphics[9] = R.drawable.item10;
			buttonGraphics[10] = R.drawable.item11;
			buttonGraphics[11] = R.drawable.item12;
			buttonGraphics[12] = R.drawable.item13;
			buttonGraphics[13] = R.drawable.item14;
			buttonGraphics[14] = R.drawable.item15;
			buttonGraphics[15] = R.drawable.item16;
			buttonGraphics[16] = R.drawable.item17;
			buttonGraphics[17] = R.drawable.item18;
		}

		shuffleButtonGraphics();
	}

	public void shuffleButtonGraphics() {
		Random random = new Random();
		int i = 0;

		for (i = 0; i < numOfElements; i++) {
			buttonGraphicLocations[i] = i % (numOfElements / 2);
		}

		for (i = 0; i < numOfElements; i++) {
			int temp = buttonGraphicLocations[i];
			int swapIndex = random.nextInt(numOfElements);
			buttonGraphicLocations[i] = buttonGraphicLocations[swapIndex];
			buttonGraphicLocations[swapIndex] = temp;
		}
	}

	@Override
	public void onClick(View view) {
		if (isBusy) {
			return;
		}

		MemoryButton button = (MemoryButton) view;

		if (button.isMatched()) {
			return;
		}

		if (selectedButton1 == null) {
			selectedButton1 = button;
			selectedButton1.flip();
			return;
		}

		if (selectedButton1.getId() == button.getId()) {
			return;
		}

		if (selectedButton1.getFrontDrawableID() == button.getFrontDrawableID()) {
			button.flip();
			button.setIsMatches(true);
			selectedButton1.setIsMatches(true);

			selectedButton1.setEnabled(false);
			button.setEnabled(false);

			selectedButton1 = null;

			numOfMatchedPair++;
			numOfTurn++;
			
			if (numOfMatchedPair == (numOfElements / 2)) {
				// Dialog inform winning game
				if(gameLevel.equalsIgnoreCase("medium")){
					gameScore = 16000 + (22-numOfTurn)*1000;
				} else{
					gameScore = 36000 + (12-numOfTurn)*1000;
				}
				
				if(gameScore<=0){
					gameScore = 1;
				}
				
				int highscore = getHighscore();
				if(gameScore>highscore){
					updateHighScore(gameScore);
					highscore = gameScore;
				}
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Congratulation! You win!")
						.setMessage("Turn: " + numOfTurn + "\nYour Score: " + gameScore + "\nHigh Score: " + highscore)
						.setPositiveButton("New game", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(getBaseContext(), MemoryPlayActivity.class);
								intent.putExtra("memoryLevel", gameLevel);
								startActivity(intent);
								MemoryPlayActivity.this.finish();
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						}).show();

			}

		} else {
			selectedButton2 = button;
			selectedButton2.flip();
			isBusy = true;
			numOfTurn++;
			final Handler handler = new Handler();

			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					selectedButton2.flip();
					selectedButton1.flip();
					selectedButton1 = null;
					selectedButton2 = null;
					isBusy = false;
				}

			}, 500);
		}

		
		txtTurnInfo.setText(numOfTurn + " turns");

	}

	public void clickHowToPlay(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("How to play?").setMessage("Tap on the card to find the matched pair.")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	public void clickToNewGame(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("New game").setMessage("Start a new game? Are you sure?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getBaseContext(), MemoryPlayActivity.class);
						intent.putExtra("memoryLevel", gameLevel);
						startActivity(intent);
						MemoryPlayActivity.this.finish();
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}
}