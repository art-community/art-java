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

package ru.art.configurator.dao;

import static java.util.UUID.randomUUID;
import static ru.art.configurator.constants.ConfiguratorDbConstants.TOKEN_KEY;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.getString;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.put;

public interface UserDao {
    static void saveUser(String username, String password) {
        put(username, password);
        put(TOKEN_KEY, randomUUID().toString());
    }

    static boolean userExists(String username, String password) {
        return getString(username).map(password::equalsIgnoreCase).orElse(false);
    }

    static String getToken() {
        return getString(TOKEN_KEY).orElse(EMPTY_STRING);
    }
}
