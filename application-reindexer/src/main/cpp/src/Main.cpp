#include "Main.h"
#include "../reindexer/cpp_src/core/reindexer.h"

using namespace reindexer;

Error initializeReindexer(JNIEnv* environment, jclass currentClass, const char* directory);
const char* getClassName(JNIEnv* environment, jclass currentClass);

jobject newReindexerError(JNIEnv* environment, const Error &error)
{
    jclass reindexerErrorClass = environment->FindClass("ReindexerError");
    jmethodID constructor = environment->GetMethodID(reindexerErrorClass, "<init>", "(ILjava/lang/String;)V");
    jvalue arguments[2];
    arguments[0].i = error.code();
    arguments[1].l = environment->NewStringUTF(error.what().c_str());
    return environment->NewObjectA(reindexerErrorClass, constructor, arguments);
}

JNIEXPORT jobject JNICALL
Java_Main_initializeReindexer(JNIEnv* environment, jclass mainClass, jstring directory)
{
    return newReindexerError(environment, initializeReindexer(environment, mainClass, environment->GetStringUTFChars(directory, nullptr)));
}

Error log(JNIEnv* environment, jclass currentClass, const std::function<Error(void)> &function)
{
    auto error = function();
    printf("[JNI] [%s] Executed reindexer operation with code = '%d' and text %s \n",
           getClassName(environment, currentClass),
           error.code(),
           error.what().c_str());
    return error;
}

const char* getClassName(JNIEnv* environment, jclass currentClass)
{
    jmethodID methodId = environment->GetMethodID(currentClass, "getName", "()Ljava/lang/String;");
    auto className = (jstring) environment->CallObjectMethod(currentClass, methodId);
    return environment->GetStringUTFChars(className, nullptr);
}

Error initializeReindexer(JNIEnv* environment, jclass currentClass, const char* directory)
{
    auto reindexer = Reindexer();
    reindexer.Connect(directory, ConnectOpts());
    const Error &openNamespace = reindexer.OpenNamespace("Test");
    if (!openNamespace.ok())
    {
        const Error &addNamespace = reindexer.AddNamespace(NamespaceDef("Test"));
        printf("%s", addNamespace.what().c_str());
    }
    auto index = IndexDef("PK");
    index.opts_ = IndexOpts(kIndexOptPK);
    log(environment, currentClass, [&]() -> Error
    { return reindexer.AddIndex("Test", index); });
    auto item = reindexer.NewItem("Test");
    item["str"] = "Test";
    return log(environment, currentClass, [&]() -> Error
    { return reindexer.Insert("Test", item); });
}

int main(int argumentCount, char** arguments)
{
    return 0;
}