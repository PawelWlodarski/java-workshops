#!/bin/bash

MODE="${1:-compact}"

CLASS="com.wlodar.jeeps.jep450CompactHeaders.CompactHeadersDemo"

# Prepare dependencies (only needed once, but harmless to repeat)
mvn dependency:copy-dependencies

# Determine JVM flag based on user input
if [ "$MODE" == "compact" ]; then
  HEADER_FLAG="-XX:+UseCompactObjectHeaders"
elif [ "$MODE" == "classic" ]; then
  HEADER_FLAG="-XX:-UseCompactObjectHeaders"
else
  echo "❗ Unknown mode: $MODE"
  echo "Usage: ./run-compact.sh [compact|classic]"
  exit 1
fi

# Run with chosen object header mode
java \
  -XX:+UnlockExperimentalVMOptions \
  $HEADER_FLAG \
  -cp "target/classes:target/dependency/*" \
  $CLASS
