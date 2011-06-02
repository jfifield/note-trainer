package org.programmerplanet.notetrainer.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2011 Joseph Fifield
 */
public class NoteTrainerActivity extends Activity {

	private static final String INSTANCE_STATE_NOTE = "note";
	private static final String INSTANCE_STATE_STATUS_TEXT = "status_text";
	private static final String INSTANCE_STATE_STATUS_COLOR = "status_color";
	private static final String INSTANCE_STATE_ATTEMPTS = "attempts";
	private static final String INSTANCE_STATE_CORRECT = "correct";

	private MusicStaffView musicStaff;
	private TextView statusTextView;
	private TextView progressTextView;
	private int attempts = 0;
	private int correct = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		musicStaff = (MusicStaffView)findViewById(R.id.MusicStaff);
		statusTextView = (TextView)findViewById(R.id.StatusTextView);
		progressTextView = (TextView)findViewById(R.id.ProgressTextView);

		LinearLayout noteButtonBar = (LinearLayout)findViewById(R.id.NoteButtonBar);

		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				Button button = (Button)v;
				CharSequence text = button.getText();
				char answer = text.charAt(0);
				answered(answer);
			}
		};

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		for (char note = Note.FIRST_NOTE; note <= Note.LAST_NOTE; note++) {
			Button button = new Button(this);
			button.setLayoutParams(layoutParams);
			button.setText(Character.toString(note));
			button.setOnClickListener(listener);
			noteButtonBar.addView(button);
		}

		setRandomNote();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(INSTANCE_STATE_NOTE, musicStaff.getNote());
		outState.putString(INSTANCE_STATE_STATUS_TEXT, statusTextView.getText().toString());
		outState.putInt(INSTANCE_STATE_STATUS_COLOR, statusTextView.getTextColors().getDefaultColor());
		outState.putInt(INSTANCE_STATE_ATTEMPTS, attempts);
		outState.putInt(INSTANCE_STATE_CORRECT, correct);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		musicStaff.setNote((Note)savedInstanceState.getSerializable(INSTANCE_STATE_NOTE));
		statusTextView.setText(savedInstanceState.getString(INSTANCE_STATE_STATUS_TEXT));
		statusTextView.setTextColor(savedInstanceState.getInt(INSTANCE_STATE_STATUS_COLOR));
		attempts = savedInstanceState.getInt(INSTANCE_STATE_ATTEMPTS);
		correct = savedInstanceState.getInt(INSTANCE_STATE_CORRECT);
		setProgressText();
	}

	private void answered(char answer) {
		attempts++;
		if (answer == musicStaff.getNote().getName()) {
			correct++;
			setStatusTextCorrect();
			setRandomNote();
		} else {
			setStatusTextIncorrect();
		}
		setProgressText();
	}

	private void setStatusTextCorrect() {
		statusTextView.setText(R.string.correct);
		statusTextView.setTextColor(Color.GREEN);
	}

	private void setStatusTextIncorrect() {
		statusTextView.setText(R.string.incorrect);
		statusTextView.setTextColor(Color.RED);
	}

	private void setProgressText() {
		progressTextView.setText(correct + "/" + attempts);
	}

	private void setRandomNote() {
		Note note = musicStaff.getNote();
		while (note.equals(musicStaff.getNote())) {
			note = Note.getRandomNote();
		}
		musicStaff.setNote(note);
	}

}
