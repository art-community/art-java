
#ifndef COROUTINE_STACK_ALLOC_H
#define COROUTINE_STACK_ALLOC_H

#include <stddef.h>

void* allocate_stack(size_t minimalSize, size_t* realSize);

void free_stack(void* stack, size_t size);

#endif
