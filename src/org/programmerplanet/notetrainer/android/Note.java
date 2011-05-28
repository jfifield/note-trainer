package org.programmerplanet.notetrainer.android;

import java.util.Random;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2011 Joseph Fifield
 */
public class Note {

	public static final char FIRST_NOTE = 'A';
	public static final char LAST_NOTE = 'G';
	public static final int NOTE_COUNT = LAST_NOTE - FIRST_NOTE + 1;
	private static final int OCTAVE_RANGE = 2;

	public static final Note MIDDLE_C = new Note('C', 0);

	private static Random RANDOM = new Random();

	public static Note getRandomNote() {
		char name = (char)(FIRST_NOTE + RANDOM.nextInt(NOTE_COUNT));
		int octave = RANDOM.nextInt(OCTAVE_RANGE);
		Note note = new Note(name, octave);
		return note;
	}

	private char name;
	private int octave;

	public Note(char name, int octave) {
		this.name = name;
		this.octave = octave;
	}

	public char getName() {
		return name;
	}

	public int getOctave() {
		return octave;
	}

	public int hashCode() {
		return 31 * (31 + name) + octave;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Note other = (Note)obj;
		return (name == other.name) && (octave == other.octave);
	}

}
