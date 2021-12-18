
#ifndef COROUTINE_H
#define COROUTINE_H

/**
 * @brief Annotates all publicly visible API functions
 */
#if defined _WIN32 || defined __CYGWIN__
    #if defined BUILDING_COROUTINE
        #define COROUTINE_API __declspec(dllexport)
    #elif defined COROUTINE_DLLIMPORT
        #define COROUTINE_API __declspec(dllimport)
    #else
        #define COROUTINE_API
    #endif
#else
    #if defined BUILDING_COROUTINE && (defined __GNUC__ || defined __LCC__)
        #define COROUTINE_API __attribute__((visibility("default")))
    #else
        #define COROUTINE_API
    #endif
#endif

/**
 * @brief Annotates API functions that don't return to their callers
 */
#if defined __STDC_VERSION__ && __STDC_VERSION__ >= 201112L
    #define COROUTINE_NORETURN _Noreturn
#elif defined __GNUC__ || defined __clang__
    #define COROUTINE_NORETURN __attribute__((__noreturn__))
#else
    #define COROUTINE_NORETURN
#endif

#if defined BUILDING_COROUTINE

    #include <stddef.h>

    #if defined NDEBUG && (defined __GNUC__ || defined __clang__)
        #define COROUTINE_UNREACHABLE __builtin_unreachable()
    #else
        #define COROUTINE_UNREACHABLE do { assert(0 && "This code path should never be reached"); abort(); } while(0)
    #endif

    #ifndef __has_feature
        #define __has_feature(x) 0  /* compatibility with non-clang compilers */
    #endif

    #if defined(__SANITIZE_ADDRESS__) || __has_feature(address_sanitizer)
        #include <sanitizer/asan_interface.h>
        #define COROUTINE_ASAN
    #endif

    #if defined(COROUTINE_VALGRIND)
        #include <valgrind/valgrind.h>
        #define COROUTINE_VALGRIND_STACK_ID(name) int name;
        #define COROUTINE_VALGRIND_STACK_REGISTER(id, start, end) id = VALGRIND_STACK_REGISTER(start, end)
        #define COROUTINE_VALGRIND_STACK_DEREGISTER(id) VALGRIND_STACK_DEREGISTER(id)
    #else
        #define COROUTINE_VALGRIND_STACK_ID(name)
        #define COROUTINE_VALGRIND_STACK_REGISTER(id, start, end)
        #define COROUTINE_VALGRIND_STACK_DEREGISTER(id)
    #endif

    #ifndef offsetof
        #ifdef __GNUC__
            #define offsetof(type, field) __builtin_offsetof(type, field)
        #else
            #define offsetof(type, field) ((size_t)&(((type *)0)->field))
        #endif
    #endif
#endif

#ifdef __cplusplus
extern "C" {
#endif

#include <stddef.h>
#include <stdio.h>

/**
 * @defgroup coroutine API for working with coroutines.
 * @{
 */

/**
 * @brief State of a #coroutine_t instance.
 */
enum coroutine_state {
    SUSPENDED,  /**< The coroutine is suspended and may be resumed with #coroutine_resume. */
    RUNNING,    /**< The coroutine is currently executing and may be yielded from with #coroutine_yield. Only up to one coroutine may be running per thread at all times. */
    DEAD,       /**< The coroutine has finished executing and may be recycled with #coroutine_recycle or destroyed with #coroutine_deinit. */
};

typedef struct coroutine coroutine_t;
struct coroutine_public {
    /**
     * @brief Private data reserved for the implementation. Don't mess with it.
     * @private
     */
    void* _private[8];
};


typedef void* (* coroutine_entrypoint_t)(void* thread, void* data);

COROUTINE_API coroutine_t* coroutine_create();

COROUTINE_API void coroutine_destroy(coroutine_t* pointer);

COROUTINE_API void
coroutine_init(coroutine_t* co, size_t min_stack_size, coroutine_entrypoint_t entry_point, void* thread);

COROUTINE_API void coroutine_recycle(coroutine_t* co, coroutine_entrypoint_t entry_point);

COROUTINE_API void coroutine_deinit(coroutine_t* co);

COROUTINE_API void* coroutine_resume(coroutine_t* co, void* arg);

COROUTINE_API void* coroutine_yield(void* arg);

COROUTINE_API COROUTINE_NORETURN void coroutine_die(void* arg);

COROUTINE_API void coroutine_kill(coroutine_t* co, void* arg);

COROUTINE_API int coroutine_state(coroutine_t* co);

COROUTINE_API coroutine_t* coroutine_active(void);

COROUTINE_API void* coroutine_get_stack(coroutine_t* co, size_t* stack_size);

COROUTINE_API size_t coroutine_page_size(void);

COROUTINE_API size_t coroutine_real_stack_size(size_t min_size);

#ifdef __cplusplus
}
#endif

#endif
