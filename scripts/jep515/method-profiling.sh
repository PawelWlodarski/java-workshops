#!/bin/bash
set -e

APP_CLASS="com.wlodar.jeeps.jep515aotmethodprofiling.HelloStreamWarmup"
CLASS_DIR="target/classes"
AOT_JAR="target/hello-stream-warmup.jar"
AOT_CACHE="target/hello-stream-warmup.aot"
LOG_DIR="target/jep515-logs"

echo "Step 0: Compiling demo..."
mvn clean compile -DskipTests

mkdir -p "$LOG_DIR"

echo "Step 1: Creating slim JAR..."
cd "$CLASS_DIR"
jar cf "../../$AOT_JAR" com/wlodar/jeeps/jep515aotmethodprofiling/*.class
cd - > /dev/null

echo
echo "Step 2: Creating AOT cache with training run..."

java \
  -XX:AOTCacheOutput="$AOT_CACHE" \
  -Xlog:aot*=info:file="$LOG_DIR/aot-build.log" \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/training-run.out"

echo
echo "Step 3: Running WITHOUT AOT cache..."
/usr/bin/time -p java \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/no-aot.out" 2> "$LOG_DIR/no-aot.time"

echo
echo "Step 4: Running WITH AOT cache..."
/usr/bin/time -p java \
  -XX:AOTCache="$AOT_CACHE" \
  -Xlog:aot*=info:file="$LOG_DIR/aot-run.log" \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/with-aot.out" 2> "$LOG_DIR/with-aot.time"

echo
echo "Step 5: Results"
echo
echo "---- Output without AOT cache ----"
cat "$LOG_DIR/no-aot.out"

echo
echo "---- Output with AOT cache ----"
cat "$LOG_DIR/with-aot.out"

echo
echo "---- Total time without AOT cache ----"
cat "$LOG_DIR/no-aot.time"

echo
echo "---- Total time with AOT cache ----"
cat "$LOG_DIR/with-aot.time"

echo
echo "Done. Logs saved in $LOG_DIR"