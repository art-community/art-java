/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.logging.constants;

public interface LoggingModuleConstants {
    interface ConfigurationKeys {
        String LOGGING_SECTION = "logging";
        String LOGGING_LOGGERS_SECTION = "logging.loggers";
        String LOGGING_DEFAULT_SECTION = "logging.default";
        String WRITER_SECTION = "writer";
        String WRITERS_SECTION = "writers";
        String BUFFER_SIZE = "buffer.size";
    }

    interface Defaults {
        int DEFAULT_LOGGING_BUFFER_SIZE = 1024 * 1024;
    }
}
