/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.tarantool.constants;

import io.art.core.collection.*;
import io.art.storage.constants.StorageConstants.*;
import org.msgpack.value.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;
import static java.lang.Integer.*;
import static java.time.Duration.*;
import static org.msgpack.value.ValueFactory.*;
import java.time.*;


public interface TarantoolModuleConstants {
    Duration DEFAULT_TARANTOOL_CONNECTION_TIMEOUT = ofSeconds(60);
    Duration DEFAULT_TARANTOOL_EXECUTION_TIMEOUT = ofSeconds(30);
    int DEFAULT_TARANTOOL_PORT = 3301;
    int RECEIVERS_INITIAL_SIZE = 8129;
    int RECEIVERS_POOL_MAXIMUM = MAX_VALUE / 2048;
    int REQUEST_ID_STEP = 8;
    String DEFAULT_TARANTOOL_HOST = "localhost";
    String DEFAULT_TARANTOOL_USERNAME = "guest";
    String DEFAULT_TARANTOOL_PASSWORD = "";
    String TARANTOOL_LOGGER = "tarantool";

    interface ProtocolConstants {
        int VERSION_LENGTH = 64;
        int SALT_LENGTH = 44;
        int GREETING_LENGTH = 128;
        int SIZE_BYTES = 5;
        int SCRAMBLE_SIZE = 20;

        ImmutableIntegerValue ZERO = newInteger(0);
        ImmutableIntegerValue IPROTO_CODE = newInteger(0);
        ImmutableIntegerValue IPROTO_SYNC = newInteger(1);
        ImmutableIntegerValue IPROTO_CALL = newInteger(10);
        ImmutableIntegerValue IPROTO_USER_NAME = newInteger(35);
        ImmutableIntegerValue IPROTO_AUTH_DATA = newInteger(33);
        ImmutableIntegerValue IPROTO_TUPLE = newInteger(33);
        ImmutableIntegerValue IPROTO_FUNCTION_NAME = newInteger(34);
        ImmutableIntegerValue IPROTO_AUTH = newInteger(7);
        ImmutableIntegerValue IPROTO_OK = newInteger(0);
        ImmutableIntegerValue IPROTO_BODY_DATA = newInteger(48);
        ImmutableIntegerValue IPROTO_CHUNK = newInteger(128);
        ImmutableIntegerValue IPROTO_ERROR = newInteger(49);
    }

    interface AuthenticationMechanism {
        ImmutableStringValue CHAP_SHA1 = newString("chap-sha1");
    }

    interface ConfigurationKeys {
        String TARANTOOL_SECTION = "tarantool";
        String TARANTOOL_LOGGING_KEY = "logging";
        String TARANTOOL_CLUSTERS_SECTION = "clusters";
        String TARANTOOL_INSTANCES_SECTION = "instances";
        String TARANTOOL_INSTANCE_HOST_KEY = "host";
        String TARANTOOL_INSTANCE_PORT_KEY = "port";
        String TARANTOOL_INSTANCE_IMMUTABLE_KEY = "immutable";
        String TARANTOOL_INSTANCE_ROUTER_KEY = "router";
        String TARANTOOL_INSTANCE_USERNAME_KEY = "username";
        String TARANTOOL_INSTANCE_PASSWORD_KEY = "password";
        String TARANTOOL_INSTANCE_CONNECTION_TIMEOUT_KEY = "timeout.connection";
        String TARANTOOL_INSTANCE_EXECUTION_TIMEOUT_KEY = "timeout.execution";
    }

    interface Errors {
        String INVALID_TYPE_FOR_INDEX_PART = "Type {0} is not available for using as tarantool index part type";
    }

    interface Messages {
    }

    interface Functions {
        ImmutableStringValue SPACE_FIRST = newString("art.space.first");
        ImmutableStringValue SPACE_SELECT = newString("art.space.select");
        ImmutableStringValue SPACE_FIND = newString("art.space.find");
        ImmutableStringValue SPACE_STREAM = newString("art.space.stream");
        ImmutableStringValue SPACE_COUNT = newString("art.space.count");
        ImmutableStringValue SPACE_TRUNCATE = newString("art.space.truncate");
        ImmutableStringValue SPACE_SINGLE_DELETE = newString("art.space.single.delete");
        ImmutableStringValue SPACE_SINGLE_INSERT = newString("art.space.single.insert");
        ImmutableStringValue SPACE_SINGLE_PUT = newString("art.space.single.put");
        ImmutableStringValue SPACE_SINGLE_UPDATE = newString("art.space.single.update");
        ImmutableStringValue SPACE_SINGLE_UPSERT = newString("art.space.single.upsert");
        ImmutableStringValue SPACE_MULTIPLE_UPDATE = newString("art.space.multiple.update");
        ImmutableStringValue SPACE_MULTIPLE_PUT = newString("art.space.multiple.put");
        ImmutableStringValue SPACE_MULTIPLE_DELETE = newString("art.space.multiple.delete");
        ImmutableStringValue SPACE_MULTIPLE_INSERT = newString("art.space.multiple.insert");

        ImmutableStringValue INDEX_FIRST = newString("art.index.first");
        ImmutableStringValue INDEX_FIND = newString("art.index.find");
        ImmutableStringValue INDEX_SELECT = newString("art.index.select");
        ImmutableStringValue INDEX_STREAM = newString("art.index.stream");
        ImmutableStringValue INDEX_COUNT = newString("art.index.count");
        ImmutableStringValue INDEX_SINGLE_UPDATE = newString("art.index.single.update");
        ImmutableStringValue INDEX_MULTIPLE_UPDATE = newString("art.index.multiple.update");
        ImmutableStringValue INDEX_SINGLE_DELETE = newString("art.index.single.delete");
        ImmutableStringValue INDEX_MULTIPLE_DELETE = newString("art.index.multiple.delete");

        ImmutableStringValue SCHEMA_CREATE_INDEX = newString("art.schema.createIndex");
        ImmutableStringValue SCHEMA_CREATE_SPACE = newString("art.schema.createSpace");
        ImmutableStringValue SCHEMA_SPACES = newString("art.schema.spaces");
        ImmutableStringValue SCHEMA_DROP_INDEX = newString("art.schema.dropIndex");
        ImmutableStringValue SCHEMA_RENAME_SPACE = newString("art.schema.renameSpace");
        ImmutableStringValue SCHEMA_DROP_SPACE = newString("art.schema.dropSpace");
        ImmutableStringValue SCHEMA_INDICES = newString("art.schema.indices");
        ImmutableStringValue SCHEMA_FORMAT = newString("art.schema.format");
    }

    class StreamProtocol {
        public static class Filters {
            public ImmutableIntegerValue filterEquals = newInteger(1);
            public ImmutableIntegerValue filterNotEquals = newInteger(2);
            public ImmutableIntegerValue filterMore = newInteger(3);
            public ImmutableIntegerValue filterMoreEquals = newInteger(4);
            public ImmutableIntegerValue filterLess = newInteger(5);
            public ImmutableIntegerValue filterLessEquals = newInteger(6);
            public ImmutableIntegerValue filterBetween = newInteger(7);
            public ImmutableIntegerValue filterNotBetween = newInteger(8);
            public ImmutableIntegerValue filterIn = newInteger(9);
            public ImmutableIntegerValue filterNotIn = newInteger(10);
            public ImmutableIntegerValue filterStartsWith = newInteger(11);
            public ImmutableIntegerValue filterEndsWith = newInteger(12);
            public ImmutableIntegerValue filterContains = newInteger(13);

            public ImmutableMap<FilterOperator, ImmutableIntegerValue> filtersMapping = ImmutableMap.<FilterOperator, ImmutableIntegerValue>immutableMapBuilder()
                    .put(EQUALS, filterEquals)
                    .put(NOT_EQUALS, filterNotEquals)
                    .put(MORE, filterMore)
                    .put(MORE_EQUALS, filterMoreEquals)
                    .put(LESS, filterLess)
                    .put(LESS_EQUALS, filterLessEquals)
                    .put(BETWEEN, filterBetween)
                    .put(NOT_BETWEEN, filterNotBetween)
                    .put(IN, filterIn)
                    .put(NOT_IN, filterNotIn)
                    .put(STARTS_WITH, filterStartsWith)
                    .put(ENDS_WITH, filterEndsWith)
                    .put(CONTAINS, filterContains)
                    .build();
        }

        public final Filters filters = new Filters();

        public static class Conditions {
            public ImmutableIntegerValue conditionAnd = newInteger(1);
            public ImmutableIntegerValue conditionOr = newInteger(2);
        }

        public final Conditions conditions = new Conditions();

        public static class FilterModes {
            public ImmutableIntegerValue filterBySpace = newInteger(1);
            public ImmutableIntegerValue filterByIndex = newInteger(2);
            public ImmutableIntegerValue filterByField = newInteger(3);
            public ImmutableIntegerValue filterByFunction = newInteger(4);
            public ImmutableIntegerValue nestedFilter = newInteger(5);
        }

        public final FilterModes filterModes = new FilterModes();

        public static class FilterExpressions {
            public ImmutableIntegerValue filterExpressionField = newInteger(1);
            public ImmutableIntegerValue filterExpressionValue = newInteger(2);
        }

        public final FilterExpressions filterExpressions = new FilterExpressions();

        public static class MappingModes {
            public ImmutableIntegerValue mapBySpace = newInteger(1);
            public ImmutableIntegerValue mapByIndex = newInteger(2);
            public ImmutableIntegerValue mapByFunction = newInteger(3);
            public ImmutableIntegerValue mapByField = newInteger(4);
        }

        public final MappingModes mappingModes = new MappingModes();

        public static class Comparators {
            public ImmutableIntegerValue comparatorMore = newInteger(1);
            public ImmutableIntegerValue comparatorLess = newInteger(2);
        }

        public final Comparators comparators = new Comparators();

        public static class ProcessingFunctions {
            public ImmutableIntegerValue processingLimit = newInteger(1);
            public ImmutableIntegerValue processingOffset = newInteger(2);
            public ImmutableIntegerValue processingFilter = newInteger(3);
            public ImmutableIntegerValue processingSort = newInteger(4);
            public ImmutableIntegerValue processingDistinct = newInteger(5);
            public ImmutableIntegerValue processingMap = newInteger(6);
        }

        public final ProcessingFunctions processingFunctions = new ProcessingFunctions();

        public static class TerminatingFunctions {
            public ImmutableIntegerValue terminatingCollect = newInteger(1);
            public ImmutableIntegerValue terminatingCount = newInteger(2);
            public ImmutableIntegerValue terminatingAll = newInteger(3);
            public ImmutableIntegerValue terminatingAny = newInteger(4);
            public ImmutableIntegerValue terminatingNone = newInteger(5);
        }

        public final TerminatingFunctions terminatingFunctions = new TerminatingFunctions();
    }

    StreamProtocol STREAM_PROTOCOL = new StreamProtocol();

    enum Engine {
        MEMTEX,
        VINYL
    }

    enum FieldType {
        ANY,
        UNSIGNED,
        STRING,
        NUMBER,
        DOUBLE,
        INTEGER,
        BOOLEAN,
        DECIMAL,
        UUID,
        SCALAR,
        ARRAY,
        MAP
    }

    interface SpaceFields {
        ImmutableStringValue ID = newString("id");
        ImmutableStringValue ENGINE = newString("engine");
        ImmutableStringValue FIELD_COUNT = newString("field_count");
        ImmutableStringValue IF_NOT_EXISTS = newString("if_not_exists");
        ImmutableStringValue IS_LOCAL = newString("is_local");
        ImmutableStringValue IS_SYNC = newString("is_sync");
        ImmutableStringValue USER = newString("user");
        ImmutableStringValue TEMPORARY = newString("temporary");
        ImmutableStringValue FORMAT = newString("format");
    }

    interface IndexFields {
        ImmutableStringValue ID = newString("id");
        ImmutableStringValue TYPE = newString("type");
        ImmutableStringValue UNIQUE = newString("unique");
        ImmutableStringValue PARTS = newString("parts");
        ImmutableStringValue DIMENSION = newString("dimension");
        ImmutableStringValue DISTANCE = newString("distance");
        ImmutableStringValue BLOOM_FPR = newString("bloom_fpr");
        ImmutableStringValue PAGE_SIZE = newString("page_size");
        ImmutableStringValue RANGE_SIZE = newString("range_size");
        ImmutableStringValue RUN_COUNT_PER_LEVEL = newString("run_count_per_level");
        ImmutableStringValue RUN_SIZE_RATIO = newString("run_size_ratio");
        ImmutableStringValue SEQUENCE = newString("sequence");
        ImmutableStringValue FUNC = newString("func");
        ImmutableStringValue HINT = newString("hint");
        ImmutableStringValue IF_NOT_EXISTS = newString("if_not_exists");
    }

    interface IndexPartFields {
        ImmutableStringValue FIELD = newString("field");
        ImmutableStringValue TYPE = newString("type");
        ImmutableStringValue IS_NULLABLE = newString("is_nullable");
        ImmutableStringValue PATH = newString("path");
    }

    enum IndexType {
        HASH,
        TREE,
        BITSET,
        RTREE
    }

    interface UpdateOperation {
        ImmutableStringValue ADDITION = newString("+");
        ImmutableStringValue SUBTRACTION = newString("-");
        ImmutableStringValue BITWISE_AND = newString("&");
        ImmutableStringValue BITWISE_OR = newString("|");
        ImmutableStringValue BITWISE_XOR = newString("^");
        ImmutableStringValue SPLICE = newString(":");
        ImmutableStringValue ASSIGMENT = newString("=");
    }

    interface ShardingAlgorithm {
        ImmutableIntegerValue CRC_32 = newInteger(1);
    }
}
