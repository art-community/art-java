/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.tarantool.configuration.lua;

import lombok.Builder;
import lombok.Getter;
import org.jtwig.JtwigModel;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_PORT;
import static ru.art.tarantool.constants.TarantoolModuleConstants.JTW_EXTENSION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.CONFIGURATION;

@Getter
@Builder
@SuppressWarnings("Duplicates")
public class TarantoolInitialConfiguration {
    @Builder.Default
    private final Integer port = DEFAULT_TARANTOOL_PORT;
    private Boolean background;
    private String customProcTitle;
    private String memtxDir;
    private String vinylDir;
    private String workDir;
    private String username;
    private String pidFile;
    private Boolean readOnly;
    private Long vinylTimeout;
    private Integer workerPoolThreads;
    private Long memtexMaxTupleSize;
    private Long memtxMemory;
    private Integer slabAllocFactor;

    public String toLua() {
        return classpathTemplate(CONFIGURATION + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(LISTEN, port)
                        .with(BACKGROUND, background)
                        .with(CUSTOM_PROC_TITLE, customProcTitle)
                        .with(MEMTX_DIR, memtxDir)
                        .with(VINYL_DIR, vinylDir)
                        .with(WORK_DIR, workDir)
                        .with(USERNAME, username)
                        .with(PID_FILE, pidFile)
                        .with(READ_ONLY, readOnly)
                        .with(VINYL_TIMEOUT, vinylTimeout)
                        .with(MEMTX_MAX_TUPLE_SIZE, memtexMaxTupleSize)
                        .with(MEMTX_MEMORY, memtxMemory)
                        .with(SLAB_ALLOC_FACTOR, slabAllocFactor)
                        .with(WORKER_POOL_THREADS, workerPoolThreads));
    }
}
