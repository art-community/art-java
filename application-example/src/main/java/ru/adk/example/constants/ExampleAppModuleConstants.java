package ru.adk.example.constants;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Module constants
 * nothing interesting
 */
public interface ExampleAppModuleConstants {
    String EXAMPLE_MODULE_ID = "EXAMPLE_MODULE";
    String HTTP_SERVER_BOOTSTRAP_THREAD = "http-server-bootstrap-thread";

    interface ConfigKeys {
        String EXAMPLE_MODULE_CONFIG_SECTION_ID = "exampleModule";
        String STRING_CONFIG_FIELD = "stringConfig";
        String INT_CONFIG_FIELD = "intConfig";

        String SOAP_SECTION = "soap";
        String EXAMPLE_SOAP_SERVICE_PATH = "exampleSoapServicePath";
        String URL = "url";
    }

    interface DefaultConfigValues {
        String DEFAULT_STRING_CONFIG = "Example string configuration value";
        int DEFAULT_INT_CONFIG = 10;
    }

    interface SqlConstants {
        Table<Record> TABLE_DUAL = table("dual");
        Field<String> ONE = field("1", String.class);
    }
}
