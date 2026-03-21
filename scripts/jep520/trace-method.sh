#!/usr/bin/env bash
set -euo pipefail

MAIN_CLASS="com.wlodar.jeeps.jep520MethodTrace.MethodTraceDemo"
TRACE_FILE="jep520-method-trace.jfr"

rm -f "$TRACE_FILE"

mvn -q compile

java \
  -XX:StartFlightRecording=filename="$TRACE_FILE",settings=profile,method-trace=com.wlodar.jeeps.jep520MethodTrace.BufferService::resizeBuffer \
  -cp target/classes \
  "$MAIN_CLASS"

echo
echo "Recording created: $TRACE_FILE"
echo
jfr print --events jdk.MethodTrace --stack-depth 20 "$TRACE_FILE"