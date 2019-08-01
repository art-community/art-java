package ru.adk.config.extensions;

import ru.adk.config.Config;
import ru.adk.config.exception.ConfigException;
import static java.util.stream.Collectors.toMap;
import static ru.adk.config.ConfigProvider.config;
import static ru.adk.config.constants.ConfigExceptionMessages.SECTION_ID_IS_EMPTY;
import static ru.adk.config.remote.provider.RemoteConfigProvider.remoteConfig;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.DOT;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.extension.ExceptionExtensions.ifException;
import static ru.adk.core.extension.ExceptionExtensions.ifExceptionOrEmpty;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ConfigExtensions {
    static String configString(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getString(path);
        return config(sectionId).getString(path);
    }

    static Integer configInt(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getInt(path);
        return config(sectionId).getInt(path);
    }

    static Long configLong(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getLong(path);
        return config(sectionId).getLong(path);
    }

    static Double configDouble(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getDouble(path);
        return config(sectionId).getDouble(path);
    }

    static Boolean configBoolean(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getBool(path);
        return config(sectionId).getBool(path);
    }


    static List<String> configStringList(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getStringList(path);
        return config(sectionId).getStringList(path);
    }

    static List<Integer> configIntList(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getIntList(path);
        return config(sectionId).getIntList(path);
    }

    static List<Long> configLongList(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getLongList(path);
        return config(sectionId).getLongList(path);
    }

    static List<Double> configDoubleList(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getDoubleList(path);
        return config(sectionId).getDoubleList(path);
    }

    static List<Boolean> configBooleanList(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getBoolList(path);
        return config(sectionId).getBoolList(path);
    }


    static String configString(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getString(path);
        return config(EMPTY_STRING).getString(path);
    }

    static Integer configInt(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getInt(path);
        return config(EMPTY_STRING).getInt(path);
    }

    static Long configLong(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getLong(path);
        return config(EMPTY_STRING).getLong(path);
    }

    static Double configDouble(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getDouble(path);
        return config(EMPTY_STRING).getDouble(path);
    }

    static Boolean configBoolean(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getBool(path);
        return config(EMPTY_STRING).getBool(path);
    }


    static List<String> configStringList(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getStringList(path);
        return config(EMPTY_STRING).getStringList(path);
    }

    static List<Integer> configIntList(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getIntList(path);
        return config(EMPTY_STRING).getIntList(path);
    }

    static List<Long> configLongList(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getLongList(path);
        return config(EMPTY_STRING).getLongList(path);
    }

    static List<Double> configDoubleList(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getDoubleList(path);
        return config(EMPTY_STRING).getDoubleList(path);
    }

    static List<Boolean> configBooleanList(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getBoolList(path);
        return config(EMPTY_STRING).getBoolList(path);
    }


    static String configString(String sectionId, String path, String defaultValue) {
        return ifExceptionOrEmpty(() -> configString(sectionId, path), defaultValue);
    }

    static Integer configInt(String sectionId, String path, int defaultValue) {
        return ifExceptionOrEmpty(() -> configInt(sectionId, path), defaultValue);
    }

    static Double configDouble(String sectionId, String path, double defaultValue) {
        return ifExceptionOrEmpty(() -> configDouble(sectionId, path), defaultValue);
    }

    static Long configLong(String sectionId, String path, long defaultValue) {
        return ifExceptionOrEmpty(() -> configLong(sectionId, path), defaultValue);
    }

    static Boolean configBoolean(String sectionId, String path, boolean defaultValue) {
        return ifExceptionOrEmpty(() -> configBoolean(sectionId, path), defaultValue);
    }


    static List<String> configStringList(String sectionId, String path, List<String> defaultValue) {
        return ifExceptionOrEmpty(() -> configStringList(sectionId, path), defaultValue);
    }

    static List<Integer> configIntList(String sectionId, String path, List<Integer> defaultValue) {
        return ifExceptionOrEmpty(() -> configIntList(sectionId, path), defaultValue);
    }

    static List<Double> configDoubleList(String sectionId, String path, List<Double> defaultValue) {
        return ifExceptionOrEmpty(() -> configDoubleList(sectionId, path), defaultValue);
    }

    static List<Long> configLongList(String sectionId, String path, List<Long> defaultValue) {
        return ifExceptionOrEmpty(() -> configLongList(sectionId, path), defaultValue);
    }

    static List<Boolean> configBooleanList(String sectionId, String path, List<Boolean> defaultValue) {
        return ifExceptionOrEmpty(() -> configBooleanList(sectionId, path), defaultValue);
    }


    static Config configInner(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getConfig(path);
        Config localConfig = config(sectionId);
        return localConfig.getConfig(path);
    }

    static Config configInner(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.getConfig(path);
        Config localConfig = config(EMPTY_STRING);
        return localConfig.getConfig(path);
    }


    static <T> Map<String, T> configMap(String path, Function<Config, T> configMapper) {
        return configMap(path).entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> configMapper.apply(entry.getValue())));
    }

    @SuppressWarnings("Duplicates")
    static Map<String, Config> configMap(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig))
            return remoteConfig.getKeys(path).stream().collect(toMap(key -> key, key -> remoteConfig.getConfig(path + DOT + key)));
        Config localConfig = config(EMPTY_STRING);
        return localConfig.getKeys(path).stream().collect(toMap(key -> key, key -> localConfig.getConfig(path + DOT + key)));
    }

    static <T> Map<String, T> configMap(String sectionId, String path, Function<Config, T> configMapper) {
        return configMap(sectionId, path).entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> configMapper.apply(entry.getValue())));
    }

    @SuppressWarnings("Duplicates")
    static Map<String, Config> configMap(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) {
            return remoteConfig.getKeys(path).stream().collect(toMap(key -> key, key -> remoteConfig.getConfig(path + DOT + key)));
        }
        Config localConfig = config(sectionId);
        return localConfig.getKeys(path).stream().collect(toMap(key -> key, key -> localConfig.getConfig(path + DOT + key)));
    }


    static <T> Map<String, T> configMap(String path, Function<Config, T> configMapper, Map<String, T> defaultValues) {
        return ifException(() -> configMap(path, configMapper), defaultValues);
    }

    static <T> Map<String, T> configMap(String sectionId, String path, Function<Config, T> configMapper, Map<String, T> defaultValues) {
        return ifException(() -> configMap(sectionId, path, configMapper), defaultValues);
    }

    static boolean hasPath(String sectionId, String path) {
        if (isEmpty(sectionId)) throw new ConfigException(SECTION_ID_IS_EMPTY);
        Config remoteConfig = remoteConfig(sectionId);
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.hasPath(path);
        return config(sectionId).hasPath(path);
    }

    static boolean hasPath(String path) {
        Config remoteConfig = remoteConfig();
        if (Config.isNotEmpty(remoteConfig)) return remoteConfig.asEntityConfig().getFields().containsKey(path);
        return config(EMPTY_STRING).hasPath(path);
    }
}