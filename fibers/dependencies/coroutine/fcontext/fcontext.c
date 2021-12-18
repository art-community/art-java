#include <coroutine.h>
#include "fcontext.hpp"
#include "../stack/stack_alloc.h"

typedef struct fcontext_fiber {
    fcontext_t fctx;
    char* stack;
    size_t stack_size;
    COROUTINE_VALGRIND_STACK_ID(valgrind_stack_id)
} coroutine_fiber_t;

#include "../fiber/fiber.h"

static void coroutine_fiber_swap(coroutine_fiber_t* from, coroutine_fiber_t* to) {
    transfer_t tf = jump_fcontext(to->fctx, from);
    from = tf.data;
    from->fctx = tf.fctx;
}

static COROUTINE_NORETURN void coroutine_fiber_entry(transfer_t tf) {
    coroutine_t* co = co_current;
    assert(tf.data == &co->caller->fiber);
    ((coroutine_fiber_t*) tf.data)->fctx = tf.fctx;
    coroutine_entry(co);
}

static inline void init_fiber_fcontext(coroutine_fiber_t* fiber) {
#if defined COROUTINE_ASAN
    // ASan may get confused if the same memory region was previously used for a stack, event if it gets unmapped/free'd.
    ASAN_UNPOISON_MEMORY_REGION(fiber->stack, fiber->stack_size);
#endif

    fiber->fctx = make_fcontext(fiber->stack + fiber->stack_size, fiber->stack_size, coroutine_fiber_entry);
}

static void coroutine_fiber_init(coroutine_fiber_t* fiber, size_t min_stack_size) {
    fiber->stack = allocate_stack(min_stack_size, &fiber->stack_size);
    COROUTINE_VALGRIND_STACK_REGISTER(fiber->valgrind_stack_id, fiber->stack, fiber->stack + fiber->stack_size);
    init_fiber_fcontext(fiber);
}

static void coroutine_fiber_recycle(coroutine_fiber_t* fiber) {
    init_fiber_fcontext(fiber);
}

static void coroutine_fiber_init_main(coroutine_fiber_t* fiber) {
    (void) fiber;
}

static void coroutine_fiber_deinit(coroutine_fiber_t* fiber) {
    if (fiber->stack) {
        COROUTINE_VALGRIND_STACK_DEREGISTER(fiber->valgrind_stack_id);
        free_stack(fiber->stack, fiber->stack_size);
        fiber->stack = NULL;
    }
}

COROUTINE_API void* coroutine_get_stack(coroutine_t* co, size_t* stack_size) {
    if (stack_size) *stack_size = co->fiber.stack_size;
    return co->fiber.stack;
}
