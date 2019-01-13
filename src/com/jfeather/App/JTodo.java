package com.jfeather.App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class JTodo {
	
	/*
	 * The following declarations are for defining colored text in a terminal that supports it
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
	
	public static boolean enableColor = false;
	
	/*
	 * Since there are multiple ways to implement colors, we can allow the user to choose which type they would like to use
	 * The first option, BY_DATE, will group items by the date they are due, as to show the user what is most important to work on
	 * 
	 * The second option, which will likely be implemented in a later version (speaking from 1.5 rn) will be paired with the ability to create
	 * custom groups for tasks, and will color by grouping
	 */
	public enum ColorMode {BY_DATE, BY_GROUP, RAINBOW};
	
	public static final String FILE_PATH = "list.txt";
	public static final double VERSION = 1.5;
	
	public static final String[] FINISHED_TASKS = {"There's one less thing to do!", "Good job!",
			"Another one bites the dust!", "Git 'er done!", "You're doing great!", "Maybe now you can go to sleep!",
			"Time to binge Netflix for several hours!"};
	
	public static int[] MONTH_DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	public static void main(String[] args) {
		
		// Read in properties like whether or not we want to enable color, etc.
		readProperties();
		System.out.println("Color option: " + enableColor);
		System.out.println(Arrays.toString(args));
		
		// Create our calendar for dates and such
		Calendar date = Calendar.getInstance();
		
		// First we have to adjust for leap years
		if (date.get(Calendar.YEAR) % 4 == 0 && date.get(Calendar.YEAR) % 100 != 0)
			MONTH_DAYS[1] = 29;
		
		// First we see if the user just wants to print out the list
		if (args.length < 1) {
			// Display the todo stuff
			printList(date);
		} else {
			// If we have some arguments
			// First, we should check to see if we have an option
			if (args[0].substring(0, 1).equals("-")) {
				
				// Based on the option
				switch (args[0].substring(1, args[0].length())) {
				case "a": 
					/****** ADD *******/
					
					// Iterate until we hit -d or the end of the args
					String todoTask = "";
					int i = 1;
					boolean dateIncluded = false;
					while (i < args.length) {
						if (args[i].equals("-d")) {
							dateIncluded = true;
							break;
						}
						todoTask += args[i] + " ";
						i++;
					}
					if (todoTask.length() > 0) {
						if (dateIncluded) {
							// Look at the term after the -d
							try {
								int day = Integer.parseInt(args[i+1]) + date.get(Calendar.DAY_OF_YEAR);
								todoTask += "/" + day;
							} catch (Exception ex) {
								// First, we look to see if the user entered a day of the week
								int day = -1;
								if (args[i+1].contains("/")) {
									// We see if the user put in a date of the format "1/10"
									String[] arr = splitByString(args[i+1], "/");
									
									try {
										// Try to convert our strings to ints
										int month = Integer.parseInt(arr[0]);
										int monthDay = Integer.parseInt(arr[1]);
	
										if (date.get(Calendar.MONTH) > month - 1) {
											// TODO: Create multi-year support
											System.out.println("No support for multi-year tasks yet!");
										} else {
											// Otherwise, we find the difference in days between now and then
											for (int j = date.get(Calendar.MONTH); j < month - 1; j++) {
												// Add up the days of the months
												day += MONTH_DAYS[j];
											}
											// We now add up the days leftover
											day += (monthDay - date.get(Calendar.DAY_OF_MONTH) + 1);
											
											day += date.get(Calendar.DAY_OF_YEAR);
										}
										
									} catch (NumberFormatException e) {
										System.out.println("Invalid date of format \"Month/Day\"!");
										// Do nothing here, it just means something was wrong with the date 
									}
																	
								} else {
									switch (args[i+1].toLowerCase()) {
									case "monday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.MONDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									case "tuesday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.TUESDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.TUESDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									case "wednesday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.WEDNESDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.WEDNESDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									case "thursday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.THURSDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.THURSDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									case "friday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.FRIDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.FRIDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									case "saturday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.SATURDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.SATURDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									case "sunday":
										day = Math.abs(date.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY);
										if (date.get(Calendar.DAY_OF_WEEK) > Calendar.SUNDAY)
											day = 7 - day;
										day += date.get(Calendar.DAY_OF_YEAR);
										break;
									}
								}
								if (day != -1) {
									todoTask += "/" + day;
								} else {
									System.out.println("Invalid date provided!");
									todoTask += "/-";
								}
	
							}
						} else 
							todoTask += "/-";
						Task[] previousTasks = readList(false);
						String[] stringTasks = null;;
						
						if (previousTasks == null) {
							writeList(new String[] {todoTask});
							
						} else {
						
							stringTasks = new String[previousTasks.length + 1];
							i = 0;
							for (Task t: previousTasks) {
								stringTasks[i] = t.toString();
								i++;
							}
							stringTasks[stringTasks.length - 1] = todoTask;
							writeList(stringTasks);
						}
						System.out.println("Task added!");
						
						printList(date);
					}
					break;
				case "r":
					/****** REMOVE *******/
					
					try {
						
						int index = Integer.parseInt(args[1]);
						Task[] tasks = readList(false);
						
						String[] newTasks = new String[tasks.length - 1];
						
						int offset = 0;
						
						for (int h = 0; h < tasks.length; h++) {
							if ((h+1) == index) {
								offset = 1;
								continue;
							}
							newTasks[h-offset] = tasks[h].toString();
						}
						writeList(newTasks);
						// Decide on what to say for removing the task
						System.out.println(FINISHED_TASKS[new Random().nextInt(FINISHED_TASKS.length)]);
						
						printList(date);
						
					} catch (Exception ex) {
						System.out.println("Invalid task index!");
						ex.printStackTrace();
					}
					
					break;
				case "e": /****** EXTEND *******/
					boolean foundIndex = false;
					boolean foundExtension = false;
					int index = -1;
					try {
						
						index = (Integer.parseInt(args[1])) - 1;
						foundIndex = true;
						Task[] tasks = readList(false);
						
						int extension = (Integer.parseInt(args[2]));
						foundExtension = true;
						
						tasks[index].extendDueDate(extension);
						
						String[] newTasks = new String[tasks.length];
						
						for (int h = 0; h < tasks.length; h++) {
							newTasks[h] = tasks[h].toString();
						}
						
						writeList(newTasks);
						System.out.println("Task extended!");
						
						printList(date);
						
					} catch (Exception ex) {
						if (!foundIndex)
							System.out.println("Invalid task index!");
						if (!foundExtension && foundIndex) {
							int day = 0;
							// We can try another method to find the extension, if it is of the format "month/day"
							// We see if the user put in a date of the format "1/10"
							String[] arr = splitByString(args[2], "/");
							
							try {
								// Try to convert our strings to ints
								int month = Integer.parseInt(arr[0]);
								int monthDay = Integer.parseInt(arr[1]);

								if (date.get(Calendar.MONTH) > month - 1) {
									// TODO: Create multi-year support
									System.out.println("No support for multi-year tasks yet!");
								} else {
									// Otherwise, we find the difference in days between now and then
									for (int j = date.get(Calendar.MONTH); j < month - 1; j++) {
										// Add up the days of the months
										day += MONTH_DAYS[j];
									}
									// We now add up the days leftover
									day += (monthDay - date.get(Calendar.DAY_OF_MONTH) + 1);
									
								}
								
								// Now write the new task
								Task[] tasks = readList(false);
								tasks[index].extendDueDate(day - tasks[index].getYearDayDue() + date.get(Calendar.DAY_OF_YEAR) - 1);
								
								String[] newTasks = new String[tasks.length];
								
								for (int h = 0; h < tasks.length; h++) {
									newTasks[h] = tasks[h].toString();
								}
								
								writeList(newTasks);
								System.out.println("Task extended!");
								
								printList(date);

							} catch (NumberFormatException e) {
								System.out.println("Invalid date of format \"Month/Day\"!");
								// Do nothing here, it just means something was wrong with the date 
							}
						}
						//ex.printStackTrace();
					}

					break;
				case "p":
					/****** PRIOTIZE *******/
					try {
						
						index = (Integer.parseInt(args[1])) - 1;
						Task[] tasks = readList(false);
						
						
						String[] newTasks = new String[tasks.length];
						
						newTasks[0] = tasks[index].toString();
						int offset = 1;
						for (int h = 1; h < tasks.length; h++) {
							if (h == index + 1) {
								offset = 0;
							}
							newTasks[h] = tasks[h - offset].toString();
						}
						writeList(newTasks);
						System.out.println("Task prioritized!");
						
						printList(date);
						
					} catch (Exception ex) {
						System.out.println("Invalid task index!");
						ex.printStackTrace();
					}

					break;
				case "v":
					/****** VERSION *******/
					printVersion();
					break;
				case "h":
					/****** HELP *******/
					printHelp();
					break;
				case "c":
					/****** CLEAR *******/
					
					writeList(new String[] {});
					
					printList(date);
					break;
				case "o":
					/****** ORDER *******/
					Task[] arr = readList(false);
					insertSort(arr);
					String[] sArr = new String[arr.length];
					for (int h = 0; h < sArr.length; h++) {
						sArr[h] = arr[h].toString();
					}
					
					writeList(sArr);
					printList(date);
					
					break;
				default:
					System.out.println("Invalid task!");
					break;
				}
			} else {
				// Invalid input
				System.out.println("Invalid input!");
			}
		}
	}
	
	public static Task[] readList(boolean print) {
		
		try {
			FileReader fr = new FileReader(FILE_PATH);
			BufferedReader br = new BufferedReader(fr);
			String line;
			ArrayList<String> lines = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				// This will ignore comments (#) and config properties (:)
				if (line.length() > 0) {
					if (!line.trim().substring(0, 1).equals("#") && !line.trim().substring(0, 1).equals(":")) {
						lines.add(line);
					}
				}
			}
			
			Task[] arr = new Task[lines.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = new Task(lines.get(i));
			}
			
			br.close();
			return arr;
		} catch (IOException ex) {
			if (print) {
				if (enableColor)
					System.out.println(ANSI_RED + "Error reading file! \nFile \"" + ANSI_RESET + ANSI_WHITE + FILE_PATH + ANSI_RESET + ANSI_RED + "\" not found!");
				else
					System.out.println("Error reading file! \nFile \"" + FILE_PATH + "\" not found!");
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return null;
	}
	
	public static void readProperties() {
		try {
			FileReader fr = new FileReader(FILE_PATH);
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
	
						switch (word) {
						case "enable_color":
							//System.out.println("Found color option: " + value);
							if (value.toLowerCase().equals("true") || value.toLowerCase().equals(1))
								enableColor = true;
							else
								enableColor = false;
								
						}
					}
				}
			}
			
			br.close();
		} catch (IOException ex) {
			if (enableColor)
				System.out.println(ANSI_RED + "Error reading properties! \nFile \"" + ANSI_RESET + ANSI_WHITE + FILE_PATH + ANSI_RESET + ANSI_RED + "\" not found!");
			else
				System.out.println("Error reading properties! \nFile \"" + FILE_PATH + "\" not found!");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public static void writeList(String[] arr) {
		try {
			FileWriter fw = new FileWriter(FILE_PATH, false);
			BufferedWriter bw = new BufferedWriter(fw);
			
			// Simple disclaimer about editing
			bw.write("# The following file is generated by JTodo whenever it writes to the file, and user additions will most likely");
			bw.newLine();
			bw.write("# be overwritten on next write. To edit settings, use the config generator (todo --mkconfig) (WIP) or specific commands");
			bw.newLine();
			bw.write("# that can be found by using \"todo -h\".");
			bw.newLine();
			bw.newLine();
			
			bw.write("# Todo tasks:");
			bw.newLine();
			//System.out.println(arr.length);
			for (String s: arr) {
				bw.write(s.toString());
				bw.newLine();
			}
			
			
			/*
			 * Now we write out properties and config settings
			 */
			bw.newLine();
			bw.write("# The config settings for JTodo");
			bw.newLine();
			bw.write(":enable_color=" + enableColor + " # Possible values: true or 1 will enable, while false or 0 will disable");
			
			bw.flush();
			bw.close();
		} catch (Exception ex) {
			if (enableColor)
				System.out.println(ANSI_RED + "Error writing to file! \nFile \"" + ANSI_RESET + ANSI_WHITE + FILE_PATH + ANSI_RESET + ANSI_RED + "\" not found!");
			else
				System.out.println("Error writing to file! \nFile \"" + FILE_PATH + "\" not found!");
			//ex.printStackTrace();
		}
	}
	
	public static void printList(Calendar date) {
		/*
		 * This will be relatively simple, but most of the complexity will be due to the different options to print (color, non-color, color-bydate, etc.)
		 */
		
		Task[] tasks = readList(true);
		if (tasks != null) {
			if (tasks.length == 0) {
				
				if (enableColor)
					System.out.println(ANSI_BLUE + "Nothing to do... :)" + ANSI_RESET);
				else
					System.out.println("Nothing to do... :)");

			} else {
				int i = 0;
				
				if (enableColor)
					System.out.println(ANSI_BLUE + "******* TODO LIST *******" + ANSI_RESET);
				else
					System.out.println("******* TODO LIST *******");

				for (Task t: tasks) {
					
					t.printTask(i, date.get(Calendar.DAY_OF_YEAR), enableColor);
										
					i++;
				}
				System.out.println();
			}
		}
	}
	
	public static void printVersion() {
		// TODO; Fix this up, and add a color version
		System.out.println( "***** ******  ********  ******    ******** \n"+
							"   *    **    **    **  **    **  **    ** \n"+
							"*  *    **    **    **  **    **  **    ** \n"+
							"****    **    ********  ******    ******** \n"+
							"          Version: " + VERSION);

	}
	
	public static void printHelp() {
		printVersion();
		System.out.println("   Created by Jack Featherstone\n\n"
						 + "todo [options] [parameters]\n\n"
						 + "[EXAMPLES]\n"
						 + "todo -- Prints out current todo items\n"
						 + "todo -a [task] -- Add a new task\n"
						 + "todo -a [task] -d [n] -- Add a new task, due in n days\n\n"
						 + "[OPTIONS]\n"
						 + "-a [task]	--	Add a task to the list\n"
						 + "			If you append -d [n], it will set a due date in n days (See example)\n"
						 + "-r [n]		--	Remove the nth task from the list\n"
						 + "-e [n] [d]	--	Extend the nth task's due date by d days\n"
						 + "-p [n]		--	Priorize the nth task, moving it to the top of the list\n"
						 + "-c 		--  Clear all entries in the list\n"
						 + "-o		--	Order all of the entries in the list by due date\n"
						 + "-h		--	Show help\n"
						 + "-v		-- 	Show version");
	}
	
	public static void insertSort(Task[] arr) {
		int n = arr.length;
		Task tmp = null;
		 
		for (int i = 1; i < n; i++) {
			for (int j = i; j > 0; j--) {
				if (arr[j - 1].getYearDayDue() > arr[j].getYearDayDue()) {
					tmp = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = tmp;
				}
			}
		}
	}
	
	public static String[] splitByString(String str, String split) {
		String[] arr = new String[2];
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i+1).equals(split)) {
				arr[0] = str.substring(0, i);
				arr[1] = str.substring(i+1, str.length());
				return arr;
			}
		}
		return null;
	}
}
