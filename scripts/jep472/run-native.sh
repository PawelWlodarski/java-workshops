#!/bin/bash

set -e

CLASS="com.wlodar.jeeps.jep472JNIwarnings.NativeAbs"
LIB_DIR="libs"
CLASS_DIR="target/classes"

echo "Running $CLASS with native library from $LIB_DIR..."

java \
  -cp "$CLASS_DIR" \
  -Djava.library.path="$LIB_DIR" \
  "$CLASS"
