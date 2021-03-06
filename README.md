# jTodo

![Screenshot](https://raw.githubusercontent.com/Jfeatherstone/jTodo/master/screenshot.png)

This is just a simple CLI-based todo list made in Java. It is heavily inspired by aesophor's py-todo that can be found below:

https://github.com/aesophor/py-todo

There is also rudimentary support for colors as of version 1.5.
As of now, it only works by using a recurring set of a few colors, working on better implementation now!

## [OPTIONS]

```
$ todo -a <task> [-d <date> -g <group>]	  # Add a task n due in d days
$ todo -r [n]                             # Remove the nth task (non-group mode)
$ todo -r [n] [m]                         # Remove the mth task from the nth group (group mode)
$ todo -p [n]                             # Prioritize the nth term
$ todo -e [n] [d]                         # Extend the deadline of the nth item by d days
$ todo -c                                 # Clear all entries
$ todo -h                                 # Help and information
$ todo -v                                 # Version info
$ todo -o                                 # Order by due date
$ todo --mkconfig                         # Go through the config setup wizard
$ todo --toggle-color                     # Enable or disable color mode
$ todo --toggle-groups                    # Enable or disable group display mode
$ todo --completed                        # Print the ten most recently completed tasks
```

## [Installation]
Download the [latest release](https://github.com/Jfeatherstone/jTodo/releases) and unzip it to the directory of your choice. Run the install script (make sure you don't move the jar file into a different directory from the script) which should delete itself afterwards (currently disabled for testing, you can just delete the install script afterwards).
```
unzip jTodo.zip && ./install.sh
```
Note that zip file name may not exactly match, be sure to use the name that you download from the release page.

This will generate the folder ~/.config/jTodo/ with the jar file and uninstall script inside, along with the todo file in /usr/bin/ to call the jar file. On the first time running the program, the config creation wizard will run to setup the list file in the same directory.

## [Configuration]
The configuration file for jTodo is located at /home/USER/.config/jTodo/list.txt by default, which also contains the current and recently completed todo tasks. It is not recommended to change the settings manually, as they will likely be overwritten on the next write, and instead to use the built in settings wizard:
```
todo --mkconfig
```

