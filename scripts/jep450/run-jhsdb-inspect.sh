#!/bin/bash

set -e

CLASS="com.wlodar.jeeps.jep450CompactHeaders.CompactHeadersTarget"

echo "Compiling project..."
mvn compile

echo "Launching $CLASS..."
java \
  -XX:+UnlockExperimentalVMOptions \
  -XX:+UseCompactObjectHeaders \
  -cp target/classes \
  "$CLASS" &

APP_PID=$!
echo "Target PID: $APP_PID"
sleep 3

echo ""
echo "Opening clhsdb (HotSpot Debugger Shell)..."
echo ""
echo "Paste the following commands once clhsdb opens:"
echo ""
echo "  classes"
echo "  fields $CLASS"
echo "  print static $CLASS staticObj"
echo ""
echo "Then copy the printed address and run:"
echo "  inspect <hex address>"
echo ""
echo "To exit clhsdb: type 'quit' or press Ctrl+D"
echo ""

jhsdb clhsdb --pid "$APP_PID"
