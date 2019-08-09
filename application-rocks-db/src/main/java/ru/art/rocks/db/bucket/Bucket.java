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

package ru.art.rocks.db.bucket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.ROCKS_DB_KEY_DELIMITER;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Bucket {
    private final String name;
    private List<String> keys = dynamicArrayOf();

    public static Bucket bucket(String name) {
        return new Bucket(name);
    }

    public Bucket addKey(String key) {
        keys.add(key);
        return this;
    }

    public String dbKey() {
        StringBuilder key = new StringBuilder(name);
        for (String bucketIdentifier : keys) {
            key.append(ROCKS_DB_KEY_DELIMITER).append(bucketIdentifier);
        }
        return key.toString();
    }
}
