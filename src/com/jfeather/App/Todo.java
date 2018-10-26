package com.jfeather.App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Todo {

	public static final String FILE_PATH = "list.txt";
	public static final double VERSION = 1.1;
	
	
	public static void main(String[] args) {
		
		// Create our calendar for dates and such
		Calendar date = Calendar.getInstance();
		
		// First we see if the user just wants to print out the list
		if (args.length < 1) {
			// Display the todo stuff
			printList(date);
		} else {
			// If we have some arguments
			// First, we should check to see if we have an option
			if (args[0].substring(0, 1).equals("-")) {
				
				// Based on the option
				switch (args[0].substring(1, 2)) {
				case "a": /****** ADD *******/
					
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
					
					if (dateIncluded) {
						// Look at the term after the -d
						try {
							int day = Integer.parseInt(args[i+1]) + date.get(Calendar.DAY_OF_YEAR);
							todoTask += "/" + day;
						} catch (Exception ex) {
							// First, we look to see if the user entered a day of the week
							int day = -1;
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
					
					break;
				case "r": /****** REMOVE *******/
					
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
						System.out.println("Task removed!");
						
						printList(date);
						
					} catch (Exception ex) {
						System.out.println("Invalid task index!");
						ex.printStackTrace();
					}
					
					break;
				case "e": /****** EXTEND *******/
					try {
						
						int index = (Integer.parseInt(args[1])) - 1;
						Task[] tasks = readList(false);
						
						int extension = (Integer.parseInt(args[2]));
						
						tasks[index].extendDueDate(extension);
						
						String[] newTasks = new String[tasks.length];
						
						for (int h = 0; h < tasks.length; h++) {
							newTasks[h] = tasks[h].toString();
						}
						
						writeList(newTasks);
						System.out.println("Task extended!");
						
						printList(date);
						
					} catch (Exception ex) {
						System.out.println("Invalid task index!");
						ex.printStackTrace();
					}

					break;
				case "p": /****** PRIOTIZE *******/
					try {
						
						int index = (Integer.parseInt(args[1])) - 1;
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
				case "v": /****** VERSION *******/
					printVersion();
					break;
				case "h": /****** HELP *******/
					printHelp();
					break;
				case "c": /****** CLEAR *******/
					
					writeList(new String[] {});
					
					printList(date);
					break;
				case "o": /****** ORDER *******/
					Task[] arr = readList(false);
					insertSort(arr);
					String[] sArr = new String[arr.length];
					for (int h = 0; h < sArr.length; h++) {
						sArr[h] = arr[h].toString();
					}
					
					writeList(sArr);
					printList(date);
					
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
				lines.add(line);
			}
			
			Task[] arr = new Task[lines.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = new Task(lines.get(i));
			}
			
			return arr;
		} catch (IOException ex) {
			if (print)
				System.out.println("Error reading file! \nFile \"" + FILE_PATH + "\" not found!");
		} catch (Exception ex) {
			
		}
		return null;
	}
	
	public static void writeList(String[] arr) {
		try {
			FileWriter fw = new FileWriter(FILE_PATH, false);
			BufferedWriter bw = new BufferedWriter(fw);
			//System.out.println(arr.length);
			for (String s: arr) {
				bw.write(s.toString());
				bw.newLine();
			}
			
			bw.flush();
			
		} catch (Exception ex) {
			System.out.println("Error writing file! \nFile \"" + FILE_PATH + "\" not found!");
			ex.printStackTrace();
		}
	}
	
	public static void printList(Calendar date) {
		Task[] tasks = readList(true);
		if (tasks != null) {
			if (tasks.length == 0) {
				System.out.println("Nothing to do... :)");
			} else {
				int i = 0;
				System.out.println("******* TODO LIST *******");
				for (Task t: tasks) {
					t.printTask(i, date.get(Calendar.DAY_OF_YEAR));
					i++;
				}
				System.out.println();
			}
		}
	}
	
	public static void printVersion() {
		  System.out.println("******  ********  ******    ******** \n"+
				  			 "  **    **    **  **    **  **    ** \n"+
				  			 "  **    **    **  **    **  **    ** \n"+
		  					 "  **    ********  ******    ******** \n"+
		  					 "           Version: " + VERSION);

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
}
