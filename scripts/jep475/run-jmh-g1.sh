#!/bin/bash
set -e

CLASS="com.wlodar.jeeps.jep475G1Barriers.G1BarrierBenchmark"
CLASS_DIR="target/classes"
DEPS="target/dependency"
MAIN_CLASS="org.openjdk.jmh.Main"

echo "Compiling project and copying dependencies..."
mvn clean compile dependency:copy-dependencies -DincludeScope=runtime

echo "Running $CLASS using G1 GC..."
java \
  -cp "$CLASS_DIR:$DEPS/*" \
  -XX:+UseG1GC \
  -Xmx1G \
  "$MAIN_CLASS" "$CLASS"
