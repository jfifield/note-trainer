package org.programmerplanet.notetrainer.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2011 Joseph Fifield
 */
public class MusicStaffView extends View {

	private static final int STAFF_SPACE_HEIGHT = 16;
	private static final int STAFF_LINE_COUNT = 5;
	private static final int STAFF_SPACE_COUNT = STAFF_LINE_COUNT - 1;
	private static final int STAFF_HEIGHT = STAFF_SPACE_HEIGHT * STAFF_SPACE_COUNT;
	private static final int STAFF_LEFT_RIGHT_MARGIN = 5;

	private static final int CLEF_TOP_OFFSET = -26;
	private static final int CLEF_LEFT_MARGIN = 5;

	private static final int LEDGER_LINE_PADDING = 10;

	private int staffTop;
	private int staffBottom;
	private Bitmap trebleClef;
	private Bitmap wholeNote;
	private Note note = Note.MIDDLE_C;

	public MusicStaffView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public MusicStaffView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public MusicStaffView(Context context) {
		super(context);
		initialize();
	}

	private void initialize() {
		trebleClef = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.treble_clef);
		wholeNote = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.whole_note);
	}

	public void setNote(Note note) {
		this.note = note;
		this.invalidate();
	}

	public Note getNote() {
		return note;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		staffTop = (h / 2) - (STAFF_HEIGHT / 2);
		staffBottom = staffTop + STAFF_HEIGHT;
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec) {
		int preferred = (CLEF_LEFT_MARGIN * 2) + (STAFF_LEFT_RIGHT_MARGIN * 2) + trebleClef.getWidth() * 2;
		int measurement = getMeasurement(measureSpec, preferred);
		return measurement;
	}

	private int measureHeight(int measureSpec) {
		int preferred = STAFF_HEIGHT * 2;
		int measurement = getMeasurement(measureSpec, preferred);
		return measurement;
	}

	private int getMeasurement(int measureSpec, int preferred) {
		int specSize = MeasureSpec.getSize(measureSpec);
		int measurement = preferred;
		switch (MeasureSpec.getMode(measureSpec)) {
			case MeasureSpec.EXACTLY:
				measurement = specSize;
				break;
			case MeasureSpec.AT_MOST:
				measurement = Math.min(preferred, specSize);
				break;
		}
		return measurement;
	}

	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		drawStaff(canvas);
		drawClef(canvas);
		drawNote(canvas);
	}

	private void drawBackground(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
	}

	private void drawStaff(Canvas canvas) {
		int width = getWidth();
		Paint paint = getStaffLinePaint();
		for (int i = 0; i < STAFF_LINE_COUNT; i++) {
			int y = (i * STAFF_SPACE_HEIGHT) + staffTop;
			canvas.drawLine(STAFF_LEFT_RIGHT_MARGIN, y, width - STAFF_LEFT_RIGHT_MARGIN, y, paint);
		}
	}

	private void drawClef(Canvas canvas) {
		int left = CLEF_LEFT_MARGIN + STAFF_LEFT_RIGHT_MARGIN;
		int top = staffTop + CLEF_TOP_OFFSET;
		canvas.drawBitmap(trebleClef, left, top, null);
	}

	private void drawNote(Canvas canvas) {
		int left = ((getWidth() + trebleClef.getWidth()) / 2) - (wholeNote.getWidth() / 2);

		int middleC = staffBottom + STAFF_SPACE_HEIGHT;

		int noteOffset = Note.MIDDLE_C.getName() - note.getName();

		int noteCenterY = middleC + ((STAFF_SPACE_HEIGHT / 2) * noteOffset);
		noteCenterY = noteCenterY - ((STAFF_SPACE_HEIGHT / 2) * Note.NOTE_COUNT * note.getOctave());

		canvas.drawBitmap(wholeNote, left, noteCenterY - (STAFF_SPACE_HEIGHT / 2), null);

		Paint paint = getStaffLinePaint();

		if (noteCenterY < staffTop) {
			int ledgerLineY = staffTop;
			while ((ledgerLineY -= STAFF_SPACE_HEIGHT) >= noteCenterY) {
				canvas.drawLine(left - LEDGER_LINE_PADDING, ledgerLineY, left + wholeNote.getWidth() + LEDGER_LINE_PADDING, ledgerLineY, paint);
			}
		} else if (noteCenterY > staffBottom) {
			int ledgerLineY = staffBottom;
			while ((ledgerLineY += STAFF_SPACE_HEIGHT) <= noteCenterY) {
				canvas.drawLine(left - LEDGER_LINE_PADDING, ledgerLineY, left + wholeNote.getWidth() + LEDGER_LINE_PADDING, ledgerLineY, paint);
			}
		}
	}

	private Paint getStaffLinePaint() {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		return paint;
	}

}
