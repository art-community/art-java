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

public interface RocksDbExceptionMessages {
    String OPEN_ERROR = "Occurred error during opening RocksDb";
    String PUT_ERROR = "Occurred error during putting value into RocksDb";
    String MERGE_ERROR = "Occurred error during merging value into RocksDb";
    String GET_ERROR = "Occurred error during getting value from RocksDb";
    String DELETE_ERROR = "Occurred error during deleting value from RocksDb";
    String MESSAGE_PACK_PARSING_ERROR = "Occurred error during parsing MessagePack value from RocksDb";
}
