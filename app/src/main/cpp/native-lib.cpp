#include <jni.h>
#include <string>
#include <cassert>

extern "C" JNIEXPORT jstring JNICALL
Java_com_coderpig_drysister_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    time_t t = time(0);
    char ch[64];
    strftime(ch, sizeof(ch), "%Y-%m-%d %H-%M-%S", localtime(&t)); //年-月-日 时-分-秒
    std::string hello = ch;
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_coderpig_drysister_MainActivity_rotating(JNIEnv *env, jobject instance, jbyteArray data_) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);

    // TODO

    env->ReleaseByteArrayElements(data_, data, 0);
    jbyte a = data[0];
    jbyte b = data[1];
    jbyte c = data[2];
    return env->NewStringUTF("image");
}