package ru.art.rocks.db.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.art.rocks.db.dao.RocksDbPrimitiveDao;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.TRUE_NUMERIC;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.rocks.db.bucket.Bucket.bucket;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class RocksDbEntity {
    private final String name;
    private Map<String, String> fields = mapOf();

    public static RocksDbEntity loadDbEntity(String name, Set<String> keys) {
        RocksDbEntity entity = new RocksDbEntity(name);
        for (String key : keys) {
            if (isEmpty(key)) continue;
            Optional<String> stringOptional = RocksDbPrimitiveDao.getString(key);
            if (!stringOptional.isPresent()) {
                continue;
            }
            String value = stringOptional.get();
            if (isEmpty(value)) {
                continue;
            }
            entity.fields.put(key, value);
        }
        return entity;
    }

    public String get(String key) {
        if (isEmpty(key)) return null;
        return fields.get(key);
    }

    public Integer getInt(String key) {
        if (isEmpty(key)) return null;
        String value = fields.get(key);
        if (isEmpty(value)) return null;
        return Integer.valueOf(value);
    }

    public Long getLong(String key) {
        if (isEmpty(key)) return null;
        String value = fields.get(key);
        if (isEmpty(value)) return null;
        return Long.valueOf(value);
    }

    public Double getDouble(String key) {
        if (isEmpty(key)) return null;
        String value = fields.get(key);
        if (isEmpty(value)) return null;
        return Double.valueOf(value);
    }

    public Boolean getBoolean(String key) {
        if (isEmpty(key)) return null;
        return TRUE_NUMERIC.equals(fields.get(key));
    }

    public Character getCharacter(String key) {
        if (isEmpty(key)) return null;
        String value = fields.get(key);
        if (isEmpty(value)) return null;
        return value.charAt(0);
    }

    public RocksDbEntity put(String key, Object value) {
        if (isEmpty(key)) return this;
        if (isNull(value)) return this;
        fields.put(key, value.toString());
        return this;
    }

    public void save() {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (isEmpty(entry.getKey())) continue;
            if (isEmpty(entry.getValue())) continue;
            RocksDbPrimitiveDao.put(bucket(name).withKey(entry.getKey()).dbKey(), entry.getValue());
        }
    }
}
