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

    public Bucket withKey(String key) {
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
