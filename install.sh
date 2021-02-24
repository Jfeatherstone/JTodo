#!/bin/zsh
# This is an install script for jTodo
# If you have any questions/concerns, you can contact me at:
# jfeatherstone123@gmail.com

# This assumes that you have a .config folder in your user home folder
# If you don't you should change this variable to wherever you want to
# store your todolist and the jar file for jTodo


configDir="/home/$USER/.config/jTodo/"
if [ ! -d "$configDir" ]; then
    mkdir -p "$configDir"
fi

# Move the already compiled jar to the proper location
# When downloaded, the jar should be in the same directory as this file
mv jTodo.jar "$configDir"

cd "$configDir"

# Now create the run script
touch todo
echo "
cd /home/$USER/.config/jTodo/
java -jar jTodo.jar \$@
cd \"\$OLDPWD\"
" >> todo

# Now move the file to /usr/bin so it can be run
mv todo /usr/bin/todo

# Now allow everyone to execute the file
chmod +x /usr/bin/todo

# Now create the uninstall script
touch uninstall.sh
echo "
sudo rm /usr/bin/todo
rm -r /home/\$USER/.config/jTodo/
echo \"Config folder and command removed! If you added todo to your bashrc or zshrc file, be sure to remove it now!\"
" >> uninstall.sh

# Now allow it to be executed by anyone
chmod +x uninstall.sh

# Now tell the user the process is done
echo "Installation completed successfully, deleteing install file now..."

# Now remove this file
#rm install.sh
