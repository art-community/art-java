#include "Main.h"
#include "../reindexer/cpp_src/core/reindexer.h"

using namespace reindexer;

Error initializeReindexer(const char* directory);

jobject newReindexerError(JNIEnv* environment, const Error& error)
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
    return newReindexerError(environment, initializeReindexer(environment->GetStringUTFChars(directory, nullptr)));
}

Error initializeReindexer(const char* directory)
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
    reindexer.AddIndex("Test", index);
    printf("%s", openNamespace.what().c_str());
    auto item = reindexer.NewItem("Test");
    printf("%d", item.NumFields());
    item["str"] = "Test";
    return reindexer.Insert("Test", item);
}

int main(int argumentCount, char** arguments)
{
    initializeReindexer(nullptr);
    return 0;
}