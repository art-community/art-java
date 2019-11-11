cmake_minimum_required(VERSION 3.5)
project(application-reindexer)

set(CMAKE_DISABLE_IN_SOURCE_BUILD ON)
set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(REINDEXER_SOURCE_PATH reindexer/cpp_src)
set (REINDEXER_LIB reindexer.x64)
set (REINDEXER_EXE reindexer.x64.executable)
set(SOURCE_FILES src/Main.h src/Main.cpp)
if (MSVC)
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -wd4244 -wd4267 -wd4996 -wd4717 -MP")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -wd4244 -wd4267 -wd4996 -wd4717 -wd4800 -wd4396 -wd4503 -MP")
    set (CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} /NODEFAULTLIB:MSVCRT")
    set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} /NODEFAULTLIB:MSVCRT")
else ()
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Werror")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -Wextra -Werror -Wold-style-cast")
endif ()

add_subdirectory(reindexer/cpp_src)

find_package(JNI REQUIRED)
include_directories(
        ${LIBSRC_DIR}
        ${RESOURCES_DIR}
        ${JNI_INCLUDE_DIRS}
        ${REINDEXER_SOURCE_PATH}
        ${REINDEXER_SOURCE_PATH}/vendor)


link_directories(${REINDEXER_LINK_DIRECTORIES_GLOBAL})

add_library(${REINDEXER_LIB} SHARED ${SOURCE_FILES})
add_dependencies(${REINDEXER_LIB} reindexer)
target_link_libraries(${REINDEXER_LIB} ${REINDEXER_LIBRARIES_GLOBAL})

add_executable(${REINDEXER_EXE} ${SOURCE_FILES})
add_dependencies(${REINDEXER_EXE} reindexer)
target_link_libraries(${REINDEXER_EXE} ${REINDEXER_LIBRARIES_GLOBAL})