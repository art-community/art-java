/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.rocks.db.constants;

public interface RocksDbModuleConstants {
    String ROCKS_DB_MODULE_ID = "ROCKS_DB_MODULE";
    String DEFAULT_PATH_TO_DB = "rocks.db";
    String ROCKS_DB_KEY_DELIMITER = ":";
    String ROCKS_DB_LIST_DELIMITER = ",";

    interface DefaultOptions {
        int DEFAULT_KEY_PREFIX_BYTES = 2048;
        @SuppressWarnings("SpellCheckingInspection")
        String DEFAULT_MERGE_OPERATOR = "stringappend";
    }
}
