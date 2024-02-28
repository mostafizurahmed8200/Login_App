#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_loginapp_activity_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    std::string hello = "Welcome Here";
    return env->NewStringUTF(hello.c_str());
}