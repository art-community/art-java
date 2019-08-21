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

package ru.art.example.constants;

import org.jooq.*;

import static org.jooq.impl.DSL.*;

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
