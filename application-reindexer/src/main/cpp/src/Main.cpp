#include <cstdio>
#include "Main.h"
#include "../include/reindexer_c.h"

void initializeReindexer();

JNIEXPORT void JNICALL
Java_Main_initializeReindexer(JNIEnv *env, jobject obj)
{
    initializeReindexer();
}

void initializeReindexer()
{
    uintptr_t reindexer = init_reindexer();
    const char *string = R"(C:\\Development\\Projects\\ART\\application-reindexer\\src\\main\\c\\reindexer)";
    reindexer_string reindexerString;
    reindexerString.p = (void *) string;
    reindexerString.n = _mbstrlen(string);
    const reindexer_error &error = reindexer_connect(reindexer, reindexerString, ConnectOpts());
    printf("Result of initializing: %d with what: \"%s\"\n", error.code, error.what);
}

int main(int argumentCount, char **arguments)
{
    initializeReindexer();
    return 0;
}