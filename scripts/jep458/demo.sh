#!/usr/bin/env bash

set -e

cd "src/main/java"

echo "=== JEP 458 Demo: Multi-File Source Launch ==="
echo
echo "We are running Main.java directly."
echo "There is NO explicit 'javac' call."
echo
echo "The 'java' launcher will:"
echo " - Compile Main.java"
echo " - Automatically find and compile Helper.java"
echo " - Then run the program"
echo
echo "Command being executed:"
echo "java com/wlodar/jeeps/jep458runmultifiles/Main.java"
echo

java com/wlodar/jeeps/jep458runmultifiles/Main.java

echo
echo "Done."
echo
echo "Now modify Helper.java manually and run this script again."
echo "Notice that no manual compilation step is required."
