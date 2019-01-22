package com.jfeather.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Config {
	
	/*
	 * Options that can be accessed from this class:
	 * 
	 * - Whether or not color is enabled (isColorEnabled())
	 * - If any, what type of color is enabled (getColorType())
	 * 
	 * Also note that all config properties are written in method List.write(), along with tasks
	 */
	
	public enum ColorType {TYPE_8, TYPE_16, TYPE_256};
	
	private static boolean enableColor = false; // Default value is disabled for compatability issues
	private static String filePath = "list.txt"; // This shouldn't ever change as of v1.6
	private static ColorType colorType = ColorType.TYPE_8; // Default value is lowest number of colors
	
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
	
	public static void read() {
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
								
								break;
								
							/*
							 * COLOR TYPE
							 * 
							 * ANSI has three different levels of colors that may or may not be supported in any given terminal:
							 * 
							 * - 8 Color:
							 * 		This is the most basic type, and supports any of the following eight colors: black, red, green, yellow, blue,
							 * 		purple, cyan, and white.
							 * 
							 * - 16 Color:
							 * 		This is the intermediate level of color detail, and includes all of the colors in the 8 color set, along with a bright or bold
							 *		version of each one
							 *
							 * - 256 Color:
							 * 		The most extensive set offered by ANSI has a total of 256 colors 
							 */
							case "color_type":
								
								switch (value.trim()) {
									case "8":
										colorType = ColorType.TYPE_8;
										break;
									case "16":
										colorType = ColorType.TYPE_16;
										break;
									case "256":
										colorType = ColorType.TYPE_256;
										break;
								}
								
								break;
								
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
	
	public static void makeConfig() {
		// We essentially just run through some basic stuff and let the user configure their setup
		// We will probably have another method to configure custom colors (besides basic settings) in the Color class
		
		// Since we are going to be taking in input, we need a scanner
		Scanner sc = new Scanner(System.in);
		String input;
		jTodo.printVersion();
		System.out.println("Thanks for using jTodo! This configuration wizard is running because either the settings/task file below does not exist,\nor you have used the --mkconfig option!");
		System.out.println("A valid task file should be located at: " + (new File(filePath)).getAbsolutePath() + "\n");
		System.out.println("If you have a config file from a previous instance of this program, please exit out and move the file to the exact path shown above! \n");
		
		System.out.println("If you need to make a new config file (or overwrite an old one) please press ENTER to continue!");
		System.out.println("Note that if you are running this and there already is a config file in the location above, only settings will be overwritten and all task will be saved.");
		sc.nextLine(); // Throw away the input because we just need the user to press enter to continue
		
		System.out.println("\nAwesome! Lets make a new config file!");
		System.out.println("The first option to configure is whether or not you want jTodo to print in color. This is done through ANSI colors, and makes the output" + "\n" +
							"more attractive, though can cause some errors depending on what terminal you are using.");
		System.out.println("If you are using Windows CMD (Not cygwin) color will not work, but besides that the easiest way to tell is if the following line makes sense.");
		System.out.println("If instead of colored text, you see some invalid characters and garbage text, be sure to disable color printing!");
		System.out.println(Color.ANSI_RED + "This should be Red!" + Color.reset() + "\n");
		System.out.println("If the above line properly displayed red text, you can choose to use color or not. Otherwise, you should choose NO below. \n");
		
		while (true) {
			System.out.println("Enable color output?");
			System.out.println("(0) YES");
			System.out.println("(1) NO");
			System.out.print("Choose \"0\" or \"1\": ");
			input = sc.next();
			if (input.trim().equals("0")) {
				enableColor = true;
				break;
			}

			if (input.trim().equals("1")) {
				enableColor = false;
				break;
			}
			
			System.out.println("** Invalid selection! **");
		}
		
		// Assuming we are using color, we now have to setup what type
		if (enableColor) {
			
		}
		
		
		
		System.out.println("\nWell that's about it. Get out there are finish some tasks!");
		
		Task[] arr = List.read(false);
		
		if (arr != null) { // Preserve previous entries
			String[] strArr = new String[arr.length];
			int i = 0;
			for (Task t: arr)
				strArr[i++] = t.toString();
			
			List.write(strArr);
		} else
			List.write(new String[] {}); // Empty list
	}

}
