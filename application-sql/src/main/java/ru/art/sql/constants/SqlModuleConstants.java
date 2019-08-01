package ru.art.sql.constants;

public interface SqlModuleConstants {
    String SQL_MODULE_ID = "SQL_MODULE";

    interface ConfigurationDefaults {
        String DEFAULT_CONNECTION_TEST_QUERY = "select 1 from dual";
    }
}
