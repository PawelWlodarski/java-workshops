#!/bin/bash

set -e

PACKAGE_PATH="com/wlodar/jeeps/jep472JNIwarnings"
CLASS_NAME="NativeAbs"
JAVA_FILE="src/main/java/$PACKAGE_PATH/$CLASS_NAME.java"
NATIVE_DIR="scripts/jep472"
LIB_OUTPUT="libs"
LIB_NAME="nativeabs"

echo "Compiling Java class and generating header..."
javac -h "$NATIVE_DIR" -d target/classes "$JAVA_FILE"

HEADER_FILE="$NATIVE_DIR/${PACKAGE_PATH//\//_}_${CLASS_NAME}.h"
C_FILE="$NATIVE_DIR/${CLASS_NAME}.c"

if [ ! -f "$HEADER_FILE" ]; then
  echo "❌ Header not found: $HEADER_FILE"
  exit 1
fi

if [ ! -f "$C_FILE" ]; then
  echo "❌ C source not found: $C_FILE"
  exit 1
fi

echo "Compiling native C code..."
mkdir -p "$LIB_OUTPUT"
gcc -shared -fPIC \
  -I"${JAVA_HOME}/include" \
  -I"${JAVA_HOME}/include/linux" \
  -o "$LIB_OUTPUT/lib$LIB_NAME.so" \
  "$C_FILE"

echo "✅ Native library compiled at: $LIB_OUTPUT/lib$LIB_NAME.so"
