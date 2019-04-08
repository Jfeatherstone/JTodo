package com.jfeather.App;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;

public class Color {

	/*
	 * The following declarations are for defining colored text in a terminal that supports only 8 color ANSI
	 * We use these as follows:
	 * System.out.println(ANSI_RED + "some red text" + ANSI_RESET);
	 * 
	 * By default, color printing will be disabled, but the first time run process should hopefully deal with this properly
	 * and alert the user how to enable color
	 * 
	 * The following are some selected 256 ANSI colors that I liked, feel free to change them if you prefer another scheme
	 */
	
	public static final String ANSI_RESET = "\u001B[0m";
	
	public static final String ANSI_BLACK = "\u001b[38;5;0m";
	public static final String ANSI_RED = "\u001b[38;5;88m";
	public static final String ANSI_GREEN = "\u001b[38;5;10m";
	public static final String ANSI_YELLOW = "\u001b[38;5;221m";
	public static final String ANSI_BLUE = "\u001b[38;5;12m";
	public static final String ANSI_PURPLE = "\u001b[38;5;92m";
	public static final String ANSI_CYAN = "\u001b[38;5;14m";
	public static final String ANSI_WHITE = "\u001b[38;5;7m";
	
	// This array is used for non-group mode to show urgency as far as color
	public static final String[] ANSI_URGENCY = {ANSI_RED, ANSI_YELLOW, ANSI_GREEN, ANSI_BLUE, ANSI_PURPLE};
	
	// For group mode, we want to be able to have colors be the same for each group, varying slightly with urgency
	// because of this, we make this color array two dimensional, with each sub array containing varying hues of similar colors
	// Maximum of 5 different colored groups before the pattern repeats
	// ordered as: red, green, blue, violet, teal
	public static final int[][] ANSI_URGENCY_GROUPED = {{196, 160, 202, 208}, {2, 70, 22, 82}, {63, 27, 12, 38}, {200, 219, 111, 134}, {45, 117, 75, 105}};
	
	public static String reset() {
		return ANSI_RESET; 
	}
	
	public static String titleColor() {
		return ANSI_WHITE;
	}
	
	public static String errorColor() {
		return ANSI_RED;
	}
	
	public static String colorFromInt(int value) {
		return "\u001b[38:5:" + value + "m";
	}
	
	public static int[] relativeRankDueDates(int[] dueDates, Calendar date) {
		// This will relatively rank the due dates
		// This only returns possible values from 0-3 (since we have 4 colors per group
		int max = 0, min = 1000;
		
		
		// Find the max and min
		for (int i: dueDates) {
			if (i > max)
				max = i;
			if (i < min)
				min = i;
		}
		
		if (min == -1)
			min = date.get(Calendar.DAY_OF_YEAR);
		if (min > max)
			max = min;
		
		
		// Now adjust so that the values are relative
		for (int i = 0; i < dueDates.length; i++) {
			if (dueDates[i] != -1)
				dueDates[i] -= min;
		}
		max -= min;
		

		double increment = ((double)max) / 3;
		int[] colors = new int[dueDates.length];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < dueDates.length; j++) {
				if (dueDates[j] == -1) {
					// Least priority if there is no due date
					colors[j] = 3;
					continue;
				}
				if (dueDates[j] >= i*increment && dueDates[j] <= (i+1)*increment) {
					colors[j] = i;
					continue;
				}
			}
		}	
		
		return colors;
	}
}
