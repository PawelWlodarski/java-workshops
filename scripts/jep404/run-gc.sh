#!/bin/bash

GC_MODE="${1:-gen}"

CLASS="com.wlodar.jeeps.jep404Shenandoah.GenerationalShenandoahDemo"
HEAP_SIZE="-Xmx512m"
LOG_FLAGS="-Xlog:gc"
COMMON_FLAGS="$HEAP_SIZE $LOG_FLAGS -cp target/classes $CLASS"

echo "▶️ GC Demo: $GC_MODE"

case "$GC_MODE" in
  gen)
    java -XX:+UnlockExperimentalVMOptions \
         -XX:+UseShenandoahGC \
         -XX:ShenandoahGCMode=generational \
         $COMMON_FLAGS
    ;;
  g1)
    java -XX:+UseG1GC \
         $COMMON_FLAGS
    ;;
  zgc)
    java -XX:+UnlockExperimentalVMOptions \
         -XX:+UseZGC \
         $COMMON_FLAGS
    ;;
  *)
    echo "❗ Unknown mode: $GC_MODE"
    echo "Usage: ./scripts/jep404/run-gc.sh [gen|g1|zgc]"
    exit 1
    ;;
esac
