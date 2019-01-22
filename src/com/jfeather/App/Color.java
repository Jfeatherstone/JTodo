package com.jfeather.App;

public class Color {

	/*
	 * The following declarations are for defining colored text in a terminal that supports only 8 color ANSI
	 * We use these as follows:
	 * System.out.println(ANSI_RED + "some red text" + ANSI_RESET);
	 * 
	 * By default, color printing will be disabled, but the first time run process should hopefully deal with this properly
	 * and alert the user how to enable color
	 */
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static final String[] ANSI_RAINBOW = {ANSI_RED, ANSI_YELLOW, ANSI_GREEN, ANSI_BLUE, ANSI_PURPLE};

	/*
	 * If a terminal supports 16 color ANSI (regular and bright colors) we can also use the following
	 */
	
	/*
	 * Finally, the highest level of ANSI coloring allows for 256 different colors, through a code system
	 */
	public static final String ANSI_256_TEST = "\u001b[38;5;13m";

	public static String reset() {
		return ANSI_RESET;
	}
	
	public static String errorColor() {
		return ANSI_RED;
	}
	
}
