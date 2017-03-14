package com.example.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.GridLayout;

public class MemoryButton extends Button {
	protected int row;
	protected int column;
	protected int frontDrawableID;

	protected boolean isFlipped = false;
	protected boolean isMatched = false;

	protected Drawable frontImage;
	protected Drawable backImage;

	public MemoryButton(Context context, int row, int column, int frontDrawableID, int backDrawableID) {
		super(context);
		this.row = row;
		this.column = column;
		this.frontDrawableID = frontDrawableID;

		frontImage = getResources().getDrawable(frontDrawableID);
		backImage = getResources().getDrawable(backDrawableID);

		setBackgroundDrawable(backImage);

		GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(column));

		tempParams.width = (int) getResources().getDisplayMetrics().density * 50;
		tempParams.height = (int) getResources().getDisplayMetrics().density * 50;

		setLayoutParams(tempParams);

	}

	public boolean isMatched() {
		return isMatched;
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	public void setIsMatches(boolean isMatched) {
		this.isMatched = isMatched;
	}

	public int getFrontDrawableID() {
		return frontDrawableID;
	}

	public void flip() {
		if (isMatched) {
			return;
		}

		if (isFlipped) {

			setBackgroundDrawable(backImage);

			isFlipped = false;
		} else {

			setBackgroundDrawable(frontImage);

			isFlipped = true;
		}
	}
}
