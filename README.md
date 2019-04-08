# TodoList

![Screenshot](https://raw.githubusercontent.com/Jfeatherstone/jTodo/master/screenshot.png)

This is just a simple CLI-based todo list made in Java. It is heavily inspired by aesophor's py-todo that can be found below:

https://github.com/aesophor/py-todo

There is also rudimentary support for colors as of version 1.5.
As of now, it only works by using a recurring set of a few colors, working on better implementation now!

### [OPTIONS]
```
-a <task> [-d <date> -g <group>]   Add a task n due in d days
-r [n]                          Remove the nth task (non-group mode)
-r [n] [m]                      Remove the mth task from the nth group (group mode)
-p [n]                          Prioritize the nth term
-e [n] [d]                      Extend the deadline of the nth item by d days
-c                              Clear all entries
-h                              Help and information
-v                              Version info
-o                              Order by due date
--mkconfig                      Go through the config setup wizard
--toggle-color                  Enable or disable color mode
--toggle-groups                 Enable or disable group display mode
--completed                     Print the ten most recently completed tasks
```
