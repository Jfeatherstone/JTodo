package com.jfeather.App;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
	
	/*
	 * Options that can be accessed from this class:
	 * 
	 * - Whether or not color is enabled (isColorEnabled())
	 * - If any, what type of color is enabled (getColorType())
	 */
	public enum ColorType {TYPE_8, TYPE_16, TYPE_256};

	private static boolean enableColor;
	private static String filePath = "list.txt";
	private static ColorType colorType;
	
	/****************************************
	 * GETTERS
	 *****************************************/
	
	public static boolean isColorEnabled() {
		return enableColor;
	}
	
	public static String getFilePath() {
		return filePath;
	}
	
	public static ColorType getColorType() {
		return colorType;
	}
	
	/****************************************
	 * END GETTERS
	 *****************************************/
	
	public static void readProperties() {
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() >= 1) {
					// A colon denotes the beginning of a property definition
					if (line.substring(0, 1).contentEquals(":")) {
						int index = 1; // We start at one since the first char should be a colon

						// Grab the word before the = sign
						String word = "";
						for (int i = 0; i < line.length(); i++) {
							if (line.substring(i, i+1).equals("=")) {
								word = line.substring(index, i).trim();
								index = i;
								break;
							}
						}
						
						// This will most of the time just set index2 to the full length of the string, but just in case there is
						// a line comment, it will catch it
						int index2 = line.length();
						for (int i = index + 1; i < line.length(); i++) {
							if (line.substring(i, i+1).equals("#")) {
								index2 = i;
								break;
							}
						}
						
						// Grab the rest of the line (i.e. whatever is after the equals sign)
						String value = line.substring(index + 1, index2).trim();
						
						/*
						 * This is the part where we actually assign properties to their variables so they can be globally accessed
						 */
						switch (word) {
							/*
							 * ENABLE COLOR
							 * 
							 * This property determines whether or not the todo list will utilize different ANSI colors to make the output more
							 * attractive. For more specific information about these colors, see the Color class in the same package as this.
							 * Also see the below property, COLOR TYPE, for the different implementations of colored text
							 */
							case "enable_color":
								//System.out.println("Found color option: " + value);
								if (value.toLowerCase().equals("true") || value.toLowerCase().equals(1))
									enableColor = true;
								else
									enableColor = false;
							
							/*
							 * COLOR TYPE
							 * 
							 * ANSI has three different levels of colors that may or may not be supported in any given terminal:
							 * 
							 * - 8 Color:
							 * 		This is the most basic type, and supports any of the following eight colors: black, red, green, yellow, blue,
							 * 		purple, cyan, and white
							 */
							case "color_type":
								
						}
					}
				}
			}
			
			br.close();
		} catch (IOException ex) {
			if (enableColor)
				System.out.println(Color.errorColor() + "Error reading properties! \nFile \"" + Color.reset() + Color.ANSI_WHITE + Config.getFilePath() + Color.reset() + Color.errorColor() + "\" not found!");
			else
				System.out.println("Error reading properties! \nFile \"" + filePath + "\" not found!");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
