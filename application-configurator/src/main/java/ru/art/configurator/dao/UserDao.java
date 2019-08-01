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
