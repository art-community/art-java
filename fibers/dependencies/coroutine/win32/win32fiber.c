#include <coroutine.h>
#include <assert.h>
#include "../stack/stack_alloc.h"

#define WIN32_LEAN_AND_MEAN
#include <windows.h>

typedef struct win32_fiber {
	void *ctx;
	size_t stack_size;
} coroutine_fiber_t;

#include "../fiber/fiber.h"

static COROUTINE_NORETURN void __stdcall fiber_entry(void *data) {
	coroutine_entry(data);
}

static void coroutine_fiber_init(coroutine_fiber_t *fiber, size_t min_stack_size) {
	fiber->stack_size = min_stack_size;
	fiber->ctx = CreateFiber(fiber->stack_size, fiber_entry, fiber);
}

static void coroutine_fiber_recycle(coroutine_fiber_t *fiber) {
	DeleteFiber(fiber->ctx);
	fiber->ctx = CreateFiber(fiber->stack_size, fiber_entry, fiber);
}

static void coroutine_fiber_init_main(coroutine_fiber_t *fiber) {
	#if _WIN32_WINNT >= 0x0600
	if(IsThreadAFiber()) {
		fiber->ctx = GetCurrentFiber();
	} else {
		fiber->ctx = ConvertThreadToFiber(NULL);
	}
	#else
	fiber->ctx = ConvertThreadToFiber(NULL);
	#endif
}

static void coroutine_fiber_deinit(coroutine_fiber_t *fiber) {
	if(fiber->ctx) {
		DeleteFiber(fiber->ctx);
		fiber->ctx = NULL;
	}
}

static void coroutine_fiber_swap(coroutine_fiber_t *from, coroutine_fiber_t *to) {
	SwitchToFiber(to->ctx);
}

COROUTINE_API void *coroutine_get_stack(coroutine_t *co, size_t *stack_size) {
	if(stack_size) *stack_size = NULL;
	return NULL;
}
