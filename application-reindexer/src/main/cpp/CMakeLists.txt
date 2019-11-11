cmake_minimum_required(VERSION 3.5)
project(application-reindexer)

set(CMAKE_BUILD_TYPE Release)
if (MSVC)
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -wd4244 -wd4267 -wd4996 -wd4717 -MP")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -wd4244 -wd4267 -wd4996 -wd4717 -wd4800 -wd4396 -wd4503 -MP")
    SET (CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} /NODEFAULTLIB:MSVCRT")
else ()
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Werror")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -Wextra -Werror -Wold-style-cast")
endif ()

set(SOURCE_FILES src/Main.h src/Main.cpp)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS} include/type_consts.h include/reindexer_ctypes.h include/reindexer_c.h)

add_library(reindexer.x64 SHARED ${SOURCE_FILES})
add_executable(reindexer.executable.x64 ${SOURCE_FILES})

target_link_libraries(reindexer.x64 PRIVATE ${PROJECT_SOURCE_DIR}/dependencies/leveldb.lib ${PROJECT_SOURCE_DIR}/dependencies/reindexer.lib msvcrtd.lib vcruntimed.lib ucrtd.lib wsock32 ws2_32 shlwapi)
target_link_libraries(reindexer.executable.x64 PRIVATE ${PROJECT_SOURCE_DIR}/dependencies/leveldb.lib ${PROJECT_SOURCE_DIR}/dependencies/reindexer.lib msvcrtd.lib vcruntimed.lib ucrtd.lib wsock32 ws2_32 shlwapi)
