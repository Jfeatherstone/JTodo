#!bin/bash
# This is an install script for jTodo
# If you have any questions/concerns, you can contact me at:
# jfeatherstone123@gmail.com

# This assumes that you have a .config folder in your user home folder
# If you don't you should change this variable to wherever you want to
# store your todolist and the jar file for jTodo

dir = "/home/$USER/.config/jTodo"
if [! -d "$dir" ]; then
		mkdir -p "$dir"
fi

# Compiling
cd "$dir"
if [ -d "bin/" ]; then
		# Clean the folder
		rm -r bin/
fi

# Now make the folder
mkdir bin/

javac src/com/jfeather/App/*.java -d bin/

