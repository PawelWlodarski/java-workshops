#!/bin/bash
set -e

APP_CLASS="com.wlodar.jeeps.jep483aotclassloading.MiniApp"
CLASS_DIR="target/classes"
AOT_JAR="target/miniapp-only.jar"
AOT_CACHE="target/app.aot"
LOG_DIR="target/logs"

echo "📦 Step 0: Compiling MiniApp with Maven..."
mvn clean compile -DskipTests

mkdir -p "$LOG_DIR"

echo "📦 Step 1: Creating slim JAR with MiniApp only..."
cd "$CLASS_DIR"
jar cf "../../$AOT_JAR" com/wlodar/jeeps/jep483aotclassloading/*.class
cd - > /dev/null

echo ""
echo "🏗 Step 2: Creating AOT cache in ONE command (JEP 514)..."

# ============================================================
# CHANGED COMPARED TO JEP 483:
#
# BEFORE (JEP 483) you needed TWO separate steps:
#
#   1) Training / recording run:
#      -XX:AOTMode=record
#      -XX:AOTConfiguration=...
#
#   2) Cache creation run:
#      -XX:AOTMode=create
#      -XX:AOTConfiguration=...
#      -XX:AOTCache=...
#
# NOW (JEP 514) the common case is simplified to ONE command:
#
#   -XX:AOTCacheOutput=...
#
# What became simpler:
# - no separate AOT configuration file
# - no explicit AOTMode=record
# - no explicit AOTMode=create
# - no need to pass AOTConfiguration between steps
# - less ceremony for the same common use case
# ============================================================

java \
  -XX:AOTCacheOutput="$AOT_CACHE" \
  -Xlog:aot*=info:file="$LOG_DIR/aot-build.log" \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/app-build.out"

echo ""
echo "⚙️ Step 3: Running WITHOUT AOT cache (baseline) and measuring time..."

# ============================================================
# BASELINE RUN:
# Normal startup, no AOT cache.
# ============================================================

/usr/bin/time -p java \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/app-no-aot.out" 2> "$LOG_DIR/time-no-aot.out"

echo ""
echo "🚀 Step 4: Running WITH AOT cache and measuring time..."

# ============================================================
# CACHED RUN:
# Same app, same JAR, same main class.
# Only difference: JVM uses the AOT cache created in Step 2.
# ============================================================

/usr/bin/time -p java \
  -XX:AOTCache="$AOT_CACHE" \
  -Xlog:aot*=info:file="$LOG_DIR/aot-run.log" \
  -cp "$AOT_JAR" \
  "$APP_CLASS" > "$LOG_DIR/app-aot.out" 2> "$LOG_DIR/time-aot.out"

echo ""
echo "📊 Step 5: Showing results..."
echo ""

echo "---- Application output without AOT ----"
cat "$LOG_DIR/app-no-aot.out"

echo ""
echo "---- Application output with AOT ----"
cat "$LOG_DIR/app-aot.out"

echo ""
echo "---- Timing without AOT ----"
cat "$LOG_DIR/time-no-aot.out"

echo ""
echo "---- Timing with AOT ----"
cat "$LOG_DIR/time-aot.out"

echo ""
echo "✅ All steps completed. Logs saved to: $LOG_DIR/"
echo "✅ Created AOT cache: $AOT_CACHE"

# ============================================================
# WORKSHOP MESSAGE:
#
# JEP 483 introduced ahead-of-time class loading/linking caches.
# JEP 514 makes the COMMON command-line workflow much easier.
#
# old style: record + create
# new style: AOTCacheOutput in one step
# ============================================================