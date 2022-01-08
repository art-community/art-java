project(coroutine)
cmake_minimum_required(VERSION 3.13)
enable_language(C)

set(COROUTINE_PATH ${PROJECT_SOURCE_DIR})
set(CONTEXT_ASM_DIR "fcontext/asm")

file(GLOB_RECURSE SRCS *.h *.cc)
list(FILTER SRCS EXCLUDE REGEX "${CONTEXT_ASM_DIR}/*")

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} cmake/modules)

include(cmake/modules/TargetArch.cmake)
target_architecture(COMPILER_TARGET_ARCH)

if (UNIX)
    enable_language(ASM)
    add_definitions(-DCOROUTINE_HAVE_MMAP)
    add_definitions(-DCOROUTINE_MAP_ANONYMOUS=MAP_ANONYMOUS)
    add_definitions(-DCOROUTINE_HAVE_SYSCONF)
    add_definitions(-DCOROUTINE_SC_PAGE_SIZE=_SC_PAGE_SIZE)
    add_definitions(-DCOROUTINE_HAVE_GETPAGESIZE)
    add_definitions(-DCOROUTINE_HAVE_ALIGNED_ALLOC)
    add_definitions(-DCOROUTINE_HAVE_POSIX_MEMALIGN)
    if (APPLE)
        if (${COMPILER_TARGET_ARCH} STREQUAL "arm")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_arm_aapcs_macho_gas.S
                    ${CONTEXT_ASM_DIR}/make_arm_aapcs_macho_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "arm64" OR ${COMPILER_TARGET_ARCH} STREQUAL "aarch64")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_arm64_aapcs_macho_gas.S
                    ${CONTEXT_ASM_DIR}/make_arm64_aapcs_macho_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "x86_64")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_x86_64_sysv_macho_gas.S
                    ${CONTEXT_ASM_DIR}/make_x86_64_sysv_macho_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "i386")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_i386_sysv_macho_gas.S
                    ${CONTEXT_ASM_DIR}/make_i386_sysv_macho_gas.S
                    )
        else ()
            message(FATAL_ERROR "Unsupported APPLE-platform architecture: ${COMPILER_TARGET_ARCH}. Unable to chose context sources")
        endif ()
    else ()
        if (${COMPILER_TARGET_ARCH} STREQUAL "arm")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_arm_aapcs_elf_gas.S
                    ${CONTEXT_ASM_DIR}/make_arm_aapcs_elf_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "arm64" OR ${COMPILER_TARGET_ARCH} STREQUAL "aarch64")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_arm64_aapcs_elf_gas.S
                    ${CONTEXT_ASM_DIR}/make_arm64_aapcs_elf_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "x86_64")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_x86_64_sysv_elf_gas.S
                    ${CONTEXT_ASM_DIR}/make_x86_64_sysv_elf_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "i386")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_i386_sysv_elf_gas.S
                    ${CONTEXT_ASM_DIR}/make_i386_sysv_elf_gas.S
                    )
        elseif (NOT ${COMPILER_TARGET_ARCH} STREQUAL "e2k")
            message(FATAL_ERROR "Unsupported Linux-platform architecture: ${COMPILER_TARGET_ARCH}. Unable to chose context sources")
        endif ()
    endif ()
elseif (WIN32)
    add_definitions(-DCOROUTINE_HAVE_WIN32API)
    if (MINGW)
        enable_language(ASM)
        if (${COMPILER_TARGET_ARCH} STREQUAL "x86_64")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_x86_64_ms_pe_clang_gas.S
                    ${CONTEXT_ASM_DIR}/make_x86_64_ms_pe_clang_gas.S
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "i386")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_i386_ms_pe_clang_gas.S
                    ${CONTEXT_ASM_DIR}/make_i386_ms_pe_clang_gas.S
                    )
        else ()
            message(FATAL_ERROR "Unsupported WIN-platform architecture: ${COMPILER_TARGET_ARCH}. Unable to chose context sources")
        endif ()
    else ()
        enable_language(ASM_MASM)
        if (${COMPILER_TARGET_ARCH} STREQUAL "x86_64")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_x86_64_ms_pe_masm.asm
                    ${CONTEXT_ASM_DIR}/make_x86_64_ms_pe_masm.asm
                    )
        elseif (${COMPILER_TARGET_ARCH} STREQUAL "i386")
            list(APPEND CONTEXT_ASM_SRCS
                    ${CONTEXT_ASM_DIR}/jump_i386_ms_pe_masm.asm
                    ${CONTEXT_ASM_DIR}/make_i386_ms_pe_masm.asm
                    )
        else ()
            message(FATAL_ERROR "Unsupported WIN-platform architecture: ${COMPILER_TARGET_ARCH}. Unable to chose context sources")
        endif ()
    endif ()
else ()
    message(FATAL_ERROR "Unsupported platform. Unable to chose context sources")
endif ()
list(APPEND SRCS ${CONTEXT_ASM_SRCS})

add_definitions(-DBUILDING_COROUTINE)

include_directories(include)

list(APPEND SRCS
        ${COROUTINE_PATH}/include/coroutine.h
        ${COROUTINE_PATH}/fiber/fiber.h
        ${COROUTINE_PATH}/stack/stack_alloc.c
        ${COROUTINE_PATH}/stack/stack_alloc.h
        ${COROUTINE_PATH}/fcontext/fcontext.c
        ${COROUTINE_PATH}/fcontext/fcontext.hpp
        )

add_library(coroutine STATIC)
target_sources(coroutine PUBLIC ${SRCS})
