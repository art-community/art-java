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
    String DEFAULT_PATH_TO_DB = "rocks-db";
    String ROCKS_DB_KEY_DELIMITER = ":";
    String ROCKS_DB_LIST_DELIMITER = ",";

    interface DefaultOptions {
        int DEFAULT_KEY_PREFIX_BYTES = 2048;
        @SuppressWarnings("SpellCheckingInspection")
        String DEFAULT_MERGE_OPERATOR = "stringappend";
    }

    interface ExceptionMessages {
        String OPEN_ERROR = "Occurred error during opening RocksDb";
        String PUT_ERROR = "Occurred error during putting value into RocksDb";
        String MERGE_ERROR = "Occurred error during merging value into RocksDb";
        String GET_ERROR = "Occurred error during getting value from RocksDb";
        String DELETE_ERROR = "Occurred error during deleting value from RocksDb";
        String MESSAGE_PACK_PARSING_ERROR = "Occurred error during parsing MessagePack value from RocksDb";
    }

    interface LoggingMessages {
        String MERGE_OPERATION = "Executing MERGE operation in Rocks DB. Merging bucket with key: ''{0}'' and value: ''{1}''";
        String PUT_OPERATION = "Executing PUT operation in Rocks DB. Putting bucket with key: ''{0}'' and value: ''{1}''";
        String DELETE_OPERATION = "Executing DELETE operation in Rocks DB. Deleting bucket with key: ''{0}''";
        String DELETE_BY_PREFIX_OPERATION = "Executing DELETE_BY_PREFIX operation in Rocks DB. Deleting bucket where key starts with: ''{0}''";
        String GET_START_OPERATION = "Executing GET operation in Rocks DB. Getting bucket with key: ''{0}''";
        String GET_END_OPERATION = "Executed GET operation in Rocks DB. Got value ''{0}'' bucket with key: ''{1}''";
    }
}
