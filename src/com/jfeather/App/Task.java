package com.jfeather.App;

import java.util.Date;

public class Task {

	private String description;
	private int yearDayDue;
	
	public Task(String unparsedLine) {
		String[] arr = splitBySlash(unparsedLine);
		description = arr[0];
		if (arr[1].equals("-"))
			yearDayDue = -1;
		else
			yearDayDue = Integer.parseInt(arr[1]);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void extendDueDate(int numDays) {
		yearDayDue += numDays;
	}
	
	public String getDaysUntilDue(int dayOfYear) {
		if (yearDayDue - dayOfYear > 2)
			return yearDayDue - dayOfYear + " days";
		else if (yearDayDue - dayOfYear == 1)
			return "Tomorrow";
		else if (yearDayDue - dayOfYear == 0)
			return "Today";
		else
			return "Past Due!";
	}
	
	public void printTask(int index, int dayOfYear) {
		if (yearDayDue != -1)
			System.out.println(index + 1 + ". " + description + " (" + getDaysUntilDue(dayOfYear) + ")");
		else
			System.out.println(index + 1 + ". " + description);
	}
	
	private String[] splitBySlash(String str) {
		int index = str.length();
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i+1).equals("/")) {
				index = i;
				break;
			}
		}
		
		String[] arr = new String[2];
		arr[0] = str.substring(0, index);
		arr[1] = str.substring(index + 1, str.length());
		
		return arr;
	}
	
	@Override
	public String toString() {
		return description + "/" + yearDayDue;
	}
}
