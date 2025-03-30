#!/bin/bash
set -e

APP_CLASS="com.wlodar.jeeps.jep483aotclassloading.MiniApp"
CLASS_DIR="target/classes"
AOT_JAR="target/miniapp-only.jar"
AOT_CONF="target/app.aotconf"
AOT_CACHE="target/app.aot"
LOG_DIR="target/logs"

echo "📦 Step 0: Compiling MiniApp with Maven..."
mvn clean compile -DskipTests

# ✅ Create logs folder AFTER clean
mkdir -p "$LOG_DIR"

echo "📦 Step 1: Creating slim JAR with MiniApp only..."
cd "$CLASS_DIR"
jar cf "../../$AOT_JAR" com/wlodar/jeeps/jep483aotclassloading/*.class
cd - > /dev/null

echo ""
echo "🛠 Step 2: Recording class linking (AOTMode=record)..."
java \
  -XX:AOTMode=record \
  -XX:AOTConfiguration="$AOT_CONF" \
  -Xlog:aot=info:file="$LOG_DIR/aot-record.log" \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/app-record.out"

echo ""
echo "CREATED ARCHIVE : $AOT_CONF"
echo "📦 AOT recorded classes from MiniApp package:"
grep 'com/wlodar/jeeps/jep483aotclassloading' "$AOT_CONF" || true

echo ""
echo "🏗 Step 3: Creating AOT cache from recorded config (AOTMode=create)..."
java \
  -XX:AOTMode=create \
  -XX:AOTConfiguration="$AOT_CONF" \
  -XX:AOTCache="$AOT_CACHE" \
  -Xlog:aot*=info:file="$LOG_DIR/aot-create.log" \
  -cp "$AOT_JAR" > "$LOG_DIR/app-create.out"

echo ""
echo "🚀 Step 4: Running with AOT cache (AOTMode=on)..."
java \
  -XX:AOTMode=on \
  -XX:AOTCache="$AOT_CACHE" \
  -Xlog:aot*=info:file="$LOG_DIR/aot-run.log" \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/app-run.out"

echo ""
echo "✅ All steps completed. Logs saved to: $LOG_DIR/"