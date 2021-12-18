
#include <coroutine.h>
#include "stack_alloc.h"

#include <stdlib.h>

#if defined COROUTINE_HAVE_WIN32API
    #define WIN32_LEAN_AND_MEAN
    #include <windows.h>
    #include <memoryapi.h>
    #include <sysinfoapi.h>
#else
    #if defined COROUTINE_HAVE_MMAP

        #include <sys/mman.h>

    #endif

    #if defined COROUTINE_HAVE_SYSCONF || defined COROUTINE_HAVE_GETPAGESIZE

        #include <unistd.h>

    #endif
#endif

static inline size_t get_page_size(void) {
#if defined COROUTINE_STATIC_PAGE_SIZE
    return COROUTINE_STATIC_PAGE_SIZE;
#elif defined COROUTINE_HAVE_WIN32API
    SYSTEM_INFO si;
    GetSystemInfo(&si);
    return si.dwPageSize;
#elif defined COROUTINE_HAVE_SYSCONF
    return sysconf(COROUTINE_SC_PAGE_SIZE);
#elif defined COROUTINE_HAVE_GETPAGESIZE
    return getpagesize();
#else
    #error No way to detect page size
#endif
}

static inline void* allocate_stack_memory(size_t size) {
#if defined COROUTINE_HAVE_WIN32API
    return VirtualAlloc(NULL, size, MEM_COMMIT, PAGE_READWRITE);
#elif defined COROUTINE_HAVE_MMAP
    return mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_PRIVATE | COROUTINE_MAP_ANONYMOUS, -1, 0);
#elif defined COROUTINE_HAVE_ALIGNED_ALLOC
    return aligned_alloc(coroutine_page_size(), size);
#elif defined COROUTINE_HAVE_POSIX_MEMALIGN
    void *p = NULL;
    posix_memalign(&p, coroutine_page_size(), size);
    return p;
#else
    #pragma GCC warning "Stack will not be aligned to page size"
    return calloc(1, size);
#endif
}

static inline void free_stack_memory(void* stack, size_t size) {
#if defined COROUTINE_HAVE_WIN32API
    (void)size;
    VirtualFree(stack, 0, MEM_RELEASE);
#elif defined COROUTINE_HAVE_MMAP
    munmap(stack, size);
#else
    (void)size;
    free(stack);
#endif
}

void* allocate_stack(size_t minimalSize, size_t* realSize) {
    size_t sz = coroutine_real_stack_size(minimalSize);
    *realSize = sz;
    return allocate_stack_memory(sz);
}

void free_stack(void* stack, size_t size) {
    free_stack_memory(stack, size);
}

COROUTINE_API size_t coroutine_page_size(void) {
    static size_t page_size = 0;

    if (!page_size) {
        page_size = get_page_size();
    }

    return page_size;
}

COROUTINE_API size_t coroutine_real_stack_size(size_t size) {
    size_t page_size = coroutine_page_size();

    if (size == 0) {
        size = 64 * 1024;
    }

    size_t num_pages = (size - 1) / page_size + 1;

    if (num_pages < 2) {
        num_pages = 2;
    }

    return num_pages * page_size;
}
