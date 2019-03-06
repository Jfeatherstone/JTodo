package com.jfeather.App;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class jTodo {
	
		
	/*
	 * Since there are multiple ways to implement colors, we can allow the user to choose which type they would like to use
	 * The first option, BY_DATE, will group items by the date they are due, as to show the user what is most important to work on
	 * 
	 * The second option, which will likely be implemented in a later version (speaking from 1.5 rn) will be paired with the ability to create
	 * custom groups for tasks, and will color by grouping
	 */
	public enum ColorMode {BY_DATE, BY_GROUP, RAINBOW};
	
	public static final String FILE_PATH = "list.txt";
	public static final double VERSION = 1.8;
	
	/*
	 * AVAILABLE OPTIONS
	 * 
	 * -a body [-d date]
	 * ADD
	 * Examples:
	 * -a Some task here 					Create "Some task here" task with no due date
	 * -a Another task to do -d 5			Create "Another task to do" due in 5 days
	 * -a "Do some homework" -d 2/1			Create "Do some homework" due on February 1st
	 * 
	 * 		This option is the only way to add a new task to the todo list, with an optional parameter to specify
	 * a due date. By default, a task without a defined expiration date will be displayed with no such parameter
	 * when the list is printed. The body of the task can be specified in separate arguments (example 1) or in a single
	 * string (example 3). If you are planning on including escape/special characters in the body, it is recommended to surround
	 * it in quotes to prevent problems with the shell interfering with the parsing.
	 * 		If a due date is to be attached to the task, there are two ways to denote a date, either by the days it will be due
	 * in, or by specifying the date in month/day format. Regardless of which method is used, the date argument should be
	 * prefaced by "-d". This string will signal the end of the todo body, and thus should be avoided as any part of the body,
	 * unless it is enclosed by double quotes
	 * 
	 * 
	 * -r index
	 * REMOVE
	 * Examples:
	 * -r 3
	 * -r 1
	 * 
	 * 		This is a pretty straight forward option, where the task at the index specified will be permanently removed from
	 * the list. The indicies used are what the ones displayed on the screen, and are 1-indexed. This means that the second example will
	 * remove whatever task is at the top of our list.
	 * 
	 * 
	 * -e index extension
	 * EXTEND
	 * Examples:
	 * -e 1 5
	 * -e 2 2/28
	 * -e 1 -2
	 * 
	 * 		The extend function will allow the user to change a due date on a previously existing task. The first argument passed to
	 * the command will be the index of the task we are modifying, and second will be either the numbers of days to extend it or a new
	 * due date formatted as month/day. The date specified can be before the previous due date to push it forward, and the extension can
	 * similarly be a negative number to reduce the numbers of days until it is due.
	 * 
	 * 
	 * -p index
	 * PRIORITIZE
	 * Examples
	 * -p 1
	 * -p 3
	 * 
	 * 		
	 */
	
	public static int[] MONTH_DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	public static void main(String[] args) {
		
		// First we check if the list file exists, and run our wizard if it doesn't
		if (!(new File(Config.getFilePath()).exists()))
			Config.makeConfig();
		
		// Read in properties like whether or not we want to enable color, etc.
		Config.read();
		
		// Create our calendar for dates and such
		Calendar date = Calendar.getInstance();
		
		// First we have to adjust for leap years
		if (date.get(Calendar.YEAR) % 4 == 0 && date.get(Calendar.YEAR) % 100 != 0)
			MONTH_DAYS[1] = 29;
		
		// First we see if the user just wants to print out the list
		if (args.length < 1) {
			// Display the todo tasks and end execution
			List.print(date);
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
									todoTask += "/-1";
								}
	
							}
						} else 
							todoTask += "/-1";
						
						Task[][] previousTasks = List.read(false);
						String[] stringTasks = null;
												
						if (previousTasks[0] == null) {
							System.out.println("null");
							List.write(new String[] {todoTask}, new String[] {});
							
						} else {
						
							// This process here is essentially just appending a task to an array
							
							// Convert our current tasks to string
							stringTasks = List.taskToStringArr(previousTasks[0]);
							
							// Create an array with one extra element (the new task)
							String[] newStringTasks = new String[stringTasks.length + 1];
							
							// Copy the old array into the new
							System.arraycopy(stringTasks, 0, newStringTasks, 0, stringTasks.length);
							
							// Add the new task
							newStringTasks[newStringTasks.length - 1] = todoTask;
							
							// Now write
							List.write(newStringTasks, List.taskToStringArr(previousTasks[1]));
						}
						System.out.println("Task added!");
						
						List.print(date);
					}
					break;
				case "r":
/****** REMOVE *******/
					
					try {
						
						int index = Integer.parseInt(args[1]);
						Task[][] tasks = List.read(false);
						
						String[] newTasks = new String[tasks[0].length - 1];
						
						int offset = 0;
						
						for (int h = 0; h < tasks[0].length; h++) {
							if ((h+1) == index) {
								offset = 1;
								continue;
							}
							newTasks[h-offset] = tasks[0][h].toString();
						}
						
						String[] completedTasks = new String[tasks[1].length + 1];
						int j = 0;
						for (Task t: tasks[1]) {
							completedTasks[j++] = t.toString();
						}
						
						// We do index - 1 because our list is 1-indexed
						tasks[0][index - 1].setDone(date.get(Calendar.DAY_OF_YEAR));
						completedTasks[completedTasks.length - 1] = tasks[0][index - 1].toString();
						
						List.write(newTasks, completedTasks);
						
						// Decide on what to say for removing the task
						System.out.println(Task.FINISHED_TASKS[new Random().nextInt(Task.FINISHED_TASKS.length)]);
						
						List.print(date);
						
					} catch (Exception ex) {
						System.out.println("Invalid task index!");
						//ex.printStackTrace();
					}
					
					break;
				case "e":
/****** EXTEND *******/
					boolean foundIndex = false;
					boolean foundExtension = false;
					int index = -1;
					try {
						
						index = (Integer.parseInt(args[1])) - 1;
						foundIndex = true;
						Task[][] tasks = List.read(false);
						
						int extension = (Integer.parseInt(args[2]));
						foundExtension = true;
						
						tasks[0][index].extendDueDate(extension);
												
						List.write(List.taskToStringArr(tasks[0]), List.taskToStringArr(tasks[1]));
						System.out.println("Task extended!");
						
						List.print(date);
						
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
								Task[][] tasks = List.read(false);
								tasks[0][index].extendDueDate(day - tasks[0][index].getYearDayDue() + date.get(Calendar.DAY_OF_YEAR) - 1);
								
								
								List.write(List.taskToStringArr(tasks[0]), List.taskToStringArr(tasks[1]));
								System.out.println("Task extended!");
								
								List.print(date);

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
						Task[][] tasks = List.read(false);
						
						
						String[] newTasks = new String[tasks[0].length];
						
						newTasks[0] = tasks[0][index].toString();
						int offset = 1;
						for (int h = 1; h < tasks[0].length; h++) {
							if (h == index + 1) {
								offset = 0;
							}
							newTasks[h] = tasks[h - offset].toString();
						}
						List.write(List.taskToStringArr(tasks[0]), List.taskToStringArr(tasks[1]));
						System.out.println("Task prioritized!");
						
						List.print(date);
						
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
					
					List.write(new String[] {}, new String[] {});
					
					List.print(date);
					break;
				case "o":
/****** ORDER *******/
					Task[][] arr = List.read(false);
					insertSort(arr[0]);
					
					List.write(List.taskToStringArr(arr[0]), List.taskToStringArr(arr[1]));
					List.print(date);
					
					break;
				case "-mkconfig":
/****** MAKE CONFIG *******/
					Config.makeConfig();
					
					break;
				case "-toggle-color":
/****** TOGGLE COLOR ******/
					Config.toggleColor();
					
					Task[][] read = List.read(false);
					if (read != null) { // Preserve previous entries
						
						List.write(List.taskToStringArr(read[0]), List.taskToStringArr(read[1]));
					} else
						List.write(new String[] {}, new String[] {}); // Empty list
					break;
				case "-completed":
/****** COMPLETED ******/
					List.printCompleted(date);
					break;
				default:
/******* INVALID *******/
					System.out.println("Invalid task!");
					break;
				}
			} else {
				// Invalid input
				System.out.println("Invalid input!");
			}
		}
	}
	
		
	
	public static void printVersion() {
		// TODO; Fix this up, and add a color version
		System.out.println( "    **  ******  ********  ******    ******** \n"+
							"    **    **    **    **  **    **  **    ** \n"+
							"**  **    **    **    **  **    **  **    ** \n"+
							"******    **    ********  ******    ******** \n"+
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
						 + "-a [task]		--	Add a task to the list\n"
						 + "				If you append -d [n], it will set a due date in n days (See example)\n"
						 + "-r [n]			--	Remove the nth task from the list\n"
						 + "-e [n] [d]		--	Extend the nth task's due date by d days\n"
						 + "-p [n]			--	Priorize the nth task, moving it to the top of the list\n"
						 + "-c 			--	Clear all entries in the list (active and completed)\n"
						 + "-o			--	Order all of the entries in the list by due date\n"
						 + "-h			--	Show help\n"
						 + "-v			--	Show version\n"
						 + "--mkconfig		--	Use the config setup wizard\n"
						 + "--completed		--	Show the ten most recently completed tasks");
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
