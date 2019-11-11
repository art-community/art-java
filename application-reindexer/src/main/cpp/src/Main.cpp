#include "Main.h"
#include "../reindexer/cpp_src/core/reindexer.h"

using namespace reindexer;

void initializeReindexer();

JNIEXPORT void JNICALL
Java_Main_initializeReindexer(JNIEnv* env, jobject obj)
{
    initializeReindexer();
}

void initializeReindexer()
{
    Reindexer reindexer;
    auto string = std::string(R"(C:\Development\Projects\ART\application-reindexer\src\main\c\reindexer)");
    auto error = reindexer.Connect(string, ConnectOpts());
    printf("Result of initializing: %d with what: \"%s\"\n", error.code(), error.what().c_str());
}

int main(int argumentCount, char** arguments)
{
    initializeReindexer();
    return 0;
}