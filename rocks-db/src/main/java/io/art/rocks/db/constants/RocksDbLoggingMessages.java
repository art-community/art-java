/*
 * ART Java
 *
 * Copyright 2019 ART
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

public interface RocksDbLoggingMessages {
    String MERGE_OPERATION = "Executing MERGE operation in Rocks DB. Merging bucket with key: ''{0}'' and value: ''{1}''";
    String PUT_OPERATION = "Executing PUT operation in Rocks DB. Putting bucket with key: ''{0}'' and value: ''{1}''";
    String DELETE_OPERATION = "Executing DELETE operation in Rocks DB. Deleting bucket with key: ''{0}''";
    String DELETE_BY_PREFIX_OPERATION = "Executing DELETE_BY_PREFIX operation in Rocks DB. Deleting bucket where key starts with: ''{0}''";
    String GET_START_OPERATION = "Executing GET operation in Rocks DB. Getting bucket with key: ''{0}''";
    String GET_END_OPERATION = "Executed GET operation in Rocks DB. Got value ''{0}'' bucket with key: ''{1}''";
}
