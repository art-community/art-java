
#ifndef COROUTINE_FIBER_H
#define COROUTINE_FIBER_H

#include <coroutine.h>
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>


typedef struct coroutine {
    coroutine_fiber_t fiber;
    coroutine_t* caller;
    void* userdata;
    coroutine_entrypoint_t entry;
    int state;
    void* thread;
} coroutine_type;

static_assert(sizeof(struct coroutine) <= sizeof(struct coroutine_public), "struct coroutine is too large");

static void coroutine_fiber_deinit(coroutine_fiber_t* fiber);

static void coroutine_fiber_init(coroutine_fiber_t* fiber, size_t min_stack_size);

static void coroutine_fiber_init_main(coroutine_fiber_t* fiber);

static void coroutine_fiber_recycle(coroutine_fiber_t* fiber);

static void coroutine_fiber_swap(coroutine_fiber_t* from, coroutine_fiber_t* to);

#if defined(_MSC_VER)

__declspec(thread) static coroutine_type co_main;
__declspec(thread) static coroutine_type* co_current;

#else
static _Thread_local coroutine_type co_main;
static _Thread_local coroutine_type* co_current;

#endif

static void coroutine_swap_coroutine(coroutine_type* from, coroutine_type* to, int state) {
    from->state = state;
    co_current = to;
    to->state = RUNNING;

    coroutine_fiber_swap(&from->fiber, &to->fiber);
}

static void coroutine_return_to_caller(coroutine_type* from, int state) {
    while (from->caller->state == DEAD) {
        from->caller = from->caller->caller;
    }

    coroutine_swap_coroutine(from, from->caller, state);
}

static inline COROUTINE_NORETURN void coroutine_entry(coroutine_type* co) {
    co->userdata = co->entry(co->thread, co->userdata);
    coroutine_return_to_caller(co, DEAD);
    COROUTINE_UNREACHABLE;
}


COROUTINE_API coroutine_type* coroutine_create() {
    return malloc(sizeof(struct coroutine));
}

COROUTINE_API void coroutine_destroy(coroutine_t* pointer) {
    free(pointer);
}

COROUTINE_API void
coroutine_init(coroutine_t* co, size_t min_stack_size, coroutine_entrypoint_t entry_point, void* thread) {
    co->state = SUSPENDED;
    co->entry = entry_point;
    co->thread = thread;
    coroutine_fiber_init(&co->fiber, min_stack_size);
}

COROUTINE_API void coroutine_recycle(coroutine_t* co, coroutine_entrypoint_t entry_point) {
    co->state = SUSPENDED;
    co->entry = entry_point;
    coroutine_fiber_recycle(&co->fiber);
}

COROUTINE_API void* coroutine_resume(coroutine_t* co, void* arg) {
    coroutine_type* prev = coroutine_active();
    co->userdata = arg;
    co->caller = prev;
    coroutine_swap_coroutine(prev, co, SUSPENDED);
    return co->userdata;
}

COROUTINE_API void* coroutine_yield(void* arg) {
    coroutine_type* co = coroutine_active();
    co->userdata = arg;
    coroutine_return_to_caller(co, SUSPENDED);
    return co->userdata;
}

COROUTINE_API COROUTINE_NORETURN void coroutine_die(void* arg) {
    coroutine_type* co = coroutine_active();
    co->userdata = arg;
    coroutine_return_to_caller(co, DEAD);
    COROUTINE_UNREACHABLE;
}

COROUTINE_API void coroutine_kill(coroutine_t* co, void* arg) {
    if (co == coroutine_active()) {
        coroutine_die(arg);
    } else {
        assert(co->state == SUSPENDED);
        co->state = DEAD;
        co->userdata = arg;
    }
}

COROUTINE_API void coroutine_deinit(coroutine_t* co) {
    coroutine_fiber_deinit(&co->fiber);
}

COROUTINE_API coroutine_type* coroutine_active(void) {
    if (!co_current) {
        co_main.state = RUNNING;
        co_current = &co_main;
        coroutine_fiber_init_main(&co_main.fiber);
    }

    return co_current;
}

COROUTINE_API int coroutine_state(coroutine_t* co) {
    return co->state;
}

#endif
