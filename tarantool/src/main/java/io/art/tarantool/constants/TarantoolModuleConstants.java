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

import lombok.*;
import org.msgpack.value.*;
import static java.lang.Integer.*;
import static java.time.Duration.*;
import static org.msgpack.value.ValueFactory.*;
import java.time.*;


public interface TarantoolModuleConstants {
    Duration DEFAULT_TARANTOOL_CONNECTION_TIMEOUT = ofSeconds(30);
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

        ImmutableIntegerValue SERVICE_ID_KEY = newInteger(0x1);
        ImmutableIntegerValue METHOD_ID_KEY = newInteger(0x2);
        ImmutableIntegerValue SERVICE_METHOD_REQUEST_KEY = newInteger(0x3);
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
        String TARANTOOL_INSTANCE_USERNAME_KEY = "username";
        String TARANTOOL_INSTANCE_PASSWORD_KEY = "password";
        String TARANTOOL_INSTANCE_CONNECTION_TIMEOUT_KEY = "connection_timeout";
    }

    interface Errors {
        String INVALID_TYPE_FOR_INDEX_PART = "Type {0} is not available for using as tarantool index part type";
    }

    interface Messages {
    }

    interface Functions {
        ImmutableStringValue SPACE_FIND_FIRST = newString("art.space.findFirst");
        ImmutableStringValue SPACE_FIND_ALL = newString("art.space.findAll");
        ImmutableStringValue SPACE_FIND = newString("art.space.find");
        ImmutableStringValue SPACE_COUNT = newString("art.space.count");
        ImmutableStringValue SPACE_TRUNCATE = newString("art.space.truncate");
        ImmutableStringValue SPACE_SINGLE_DELETE = newString("art.space.single.delete");
        ImmutableStringValue SPACE_SINGLE_INSERT = newString("art.space.single.insert");
        ImmutableStringValue SPACE_SINGLE_PUT = newString("art.space.single.put");
        ImmutableStringValue SPACE_SINGLE_UPDATE = newString("art.space.single.update");
        ImmutableStringValue SPACE_MULTIPLE_PUT = newString("art.space.multiple.put");
        ImmutableStringValue SPACE_MULTIPLE_DELETE = newString("art.space.multiple.delete");
        ImmutableStringValue SPACE_MULTIPLE_INSERT = newString("art.space.multiple.insert");
        ImmutableStringValue SCHEMA_CREATE_INDEX = newString("art.schema.createIndex");
        ImmutableStringValue SCHEMA_CREATE_SPACE = newString("art.schema.createSpace");
        ImmutableStringValue SCHEMA_SPACES = newString("art.schema.spaces");
        ImmutableStringValue SCHEMA_DROP_INDEX = newString("art.schema.dropIndex");
        ImmutableStringValue SCHEMA_RENAME_SPACE = newString("art.schema.renameSpace");
        ImmutableStringValue SCHEMA_DROP_SPACE = newString("art.schema.dropSpace");
        ImmutableStringValue SCHEMA_INDICIES = newString("art.schema.indicies");
        ImmutableStringValue SCHEMA_FORMAT = newString("art.schema.format");

    }

    interface SelectOptions {
        ImmutableStringValue LIMIT = newString("limit");
        ImmutableStringValue OFFSET = newString("offset");
        ImmutableStringValue FILTER = newString("filter");
        ImmutableStringValue SORT = newString("sort");
        ImmutableStringValue DISTINCT = newString("distinct");
    }

    interface SortOptions {
        ImmutableStringValue COMPARATOR_MORE = newString("more");
        ImmutableStringValue COMPARATOR_LESS = newString("less");
    }

    interface FilterOptions {
        ImmutableStringValue OPERATOR_EQUALS = newString("equals");
        ImmutableStringValue OPERATOR_NOT_EQUALS = newString("notEquals");
        ImmutableStringValue OPERATOR_MORE = newString("more");
        ImmutableStringValue OPERATOR_LESS = newString("less");
        ImmutableStringValue OPERATOR_IN = newString("in");
        ImmutableStringValue OPERATOR_NOT_IN = newString("notIn");
        ImmutableStringValue OPERATOR_LIKE = newString("like");
        ImmutableStringValue OPERATOR_STARTS_WITH = newString("startsWith");
        ImmutableStringValue OPERATOR_ENDS_WITH = newString("endsWith");
        ImmutableStringValue OPERATOR_CONTAINS = newString("contains");
    }

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

    enum TarantoolIndexIterator {
        EQ,
        REQ,
        GT,
        GE,
        ALL,
        LT,
        LE,
        OVERLAPS,
        NEIGHBOR
    }

    @Getter
    @AllArgsConstructor
    enum TarantoolUpdateOperator {
        ADDITION("+"),
        SUBTRACTION("-"),
        AND("&"),
        OR("|"),
        XOR("^"),
        STRING_SPLICE(":"),
        INSERTION("!"),
        DELETION("#"),
        ASSIGMENT("=");

        private final String operator;
    }
}
