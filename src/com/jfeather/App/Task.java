package com.jfeather.App;

import java.util.ArrayList;
import java.util.Arrays;

public class Task {
	
	public static final String DEFAULT_GROUP = "Todo";
	
	public static final String[] FINISHED_TASKS = {"There's one less thing to do!", "Good job!",
			"Another one bites the dust!", "Git 'er done!", "You're doing great!", "Maybe now you can go to sleep!",
			"Time to binge Netflix for several hours!"};
	
	private String description;
	private String group;
	private int yearDayDue;
	private int yearDayDone;
	
	public Task(String unparsedLine) {
		// We are going to use this twice since there are two slashes
		//System.out.println(unparsedLine);
		
		String[] arr = splitBySlash(unparsedLine);
				
		//System.out.println(Arrays.toString(arr));
		
		description = arr[0];
		if (arr[1].equals("-"))
			yearDayDue = -1;
		else
			yearDayDue = Integer.parseInt(arr[1]);
		
		// See whether the task has been completed or not
		if (arr.length > 2) {
			if (arr[2].equals("-1"))
				yearDayDone = -1;
			else
				yearDayDone = Integer.parseInt(arr[2]);
			
		} else {
			yearDayDone = -1;
		}
		
		// Now we look at the group
		if (arr.length > 3) {
			group = arr[3];
		} else {
			group = DEFAULT_GROUP;
		}

	}
	
	public String getDescription() {
		return description;
	}
	
	public void extendDueDate(int currentDay, int numDays) {
		if (yearDayDue == -1)
			yearDayDue = currentDay + numDays;
		else {
			// If the user specifies the date such that the new due date would be in the past, we just remove it altogether
			if (yearDayDue - currentDay + numDays < 0)
				yearDayDue = -1;
			else
				yearDayDue += numDays;
		}
	}
	
	public int getYearDayDue() {
		return yearDayDue;
	}
	
	public String getDaysUntilDue(int dayOfYear) {
		if (yearDayDue - dayOfYear >= 2)
			return yearDayDue - dayOfYear + " days";
		else if (yearDayDue - dayOfYear == 1)
			return "Tomorrow";
		else if (yearDayDue - dayOfYear == 0)
			return "Today";
		else
			return "Past Due!";
	}
	
	public void printTask(int index, int dayOfYear, int indentSpaces) {
		// Get the proper number of spaces
		String spaces = "";
		for (int i = 0; i < (indentSpaces - (index + "").length()); i++) {
			spaces += " ";
		}
		
		if (yearDayDue != -1)
			System.out.println(index + ". " + spaces + description + " (" + getDaysUntilDue(dayOfYear) + ")");
		else
			System.out.println(index + ". " + spaces + description);
		
	}
	
	public void printCompletedTask(int index, int currentDay) {
		String completedDate = (currentDay -yearDayDone) + " days ago";
		if (currentDay - yearDayDone == 0)
				completedDate = "Today";
		if (currentDay - yearDayDone == 1)
			completedDate = "Yesterday";

		System.out.println((index + 1) + ". " + description + " (" + completedDate + ")");
	}
	
	
	public void setDone(int currentDayOfYear) {
		yearDayDone = currentDayOfYear;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String newGroup) {
		group = newGroup;
	}
	
	
	public static String[] splitBySlash(String str) {
		ArrayList<String> words = new ArrayList<>();
		
		int index1 = 0, index2 = 0;
		int counter = 0;
		while (counter < str.length()) {
			boolean done = false;

			for (int j = index1; j < str.length(); j++) {
				if (str.substring(j, j + 1).equals("/")) {
					index2 = j + 1;
					done = true;
					break;
				}
			}
			
			if (!done)
				index2 = str.length();
			words.add(str.substring(index1, index2 - 1));
			
			done = false;
			
			for (int j = index2; j < str.length(); j++) {
				if (str.substring(j, j + 1).equals("/")) {
					index1 = j + 1;
					done = true; 
					break;
				}
			}
			
			if (!done)
				index1 = str.length();
			else
				index1 -= 1;
			
			if (index1 > str.length() || index2 >= str.length())
				break;
			
			words.add(str.substring(index2, index1));
			
			counter = ++index1;
		}
				
		String[] arr = new String[words.size()];
		int i = 0;
		for (String s: words) {
			arr[i] = s;
			i++;
		}
		
		return arr;

	}
	
	
	@Override
	public String toString() {
		return description + "/" + yearDayDue + "/" + yearDayDone + "/" + group;
	}
}
