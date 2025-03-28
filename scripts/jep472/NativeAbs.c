#include <jni.h>
#include <stdlib.h>
#include "com_wlodar_jeeps_jep472JNIwarnings_NativeAbs.h"

JNIEXPORT jint JNICALL Java_com_wlodar_jeeps_jep472JNIwarnings_NativeAbs_abs
  (JNIEnv *env, jobject obj, jint x) {
    return abs(x);
}
