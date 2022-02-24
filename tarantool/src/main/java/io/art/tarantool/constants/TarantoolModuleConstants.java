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
import static java.lang.Integer.*;
import static java.time.Duration.*;
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

        int IPROTO_CODE = 0x00;
        int IPROTO_SYNC = 0x01;
        int IPROTO_CALL = 0x0a;
        int IPROTO_USER_NAME = 0x23;
        int IPROTO_AUTH_DATA = 0x21;
        int IPROTO_TUPLE = 0x21;
        int IPROTO_FUNCTION_NAME = 0x22;
        int IPROTO_AUTH = 0x07;
        int IPROTO_OK = 0x00;
        int IPROTO_BODY_DATA = 0x30;
    }

    interface AuthenticationMechanism {
        String CHAP_SHA1 = "chap-sha1";
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
        String SPACE_FIND_FIRST = "art.space.findFirst";
        String SPACE_FIND_ALL = "art.space.findAll";
        String SPACE_FIND = "art.space.find";
        String SPACE_COUNT = "art.space.count";
        String SPACE_TRUNCATE = "art.space.truncate";
        String SPACE_SINGLE_DELETE = "art.space.single.delete";
        String SPACE_SINGLE_INSERT = "art.space.single.insert";
        String SPACE_SINGLE_PUT = "art.space.single.put";
        String SPACE_SINGLE_UPDATE = "art.space.single.update";
        String SPACE_SINGLE_UPSERT = "art.space.single.upsert";

        String SPACE_MULTIPLE_PUT = "art.space.multiple.put";
        String SPACE_MULTIPLE_DELETE = "art.space.multiple.delete";
        String SPACE_MULTIPLE_INSERT = "art.space.multiple.insert";

        String SCHEMA_CREATE_INDEX = "art.schema.createIndex";
        String SCHEMA_CREATE_SPACE = "art.schema.createSpace";
        String SCHEMA_SPACES = "art.schema.spaces";
        String SCHEMA_DROP_INDEX = "art.schema.dropIndex";
        String SCHEMA_RENAME_SPACE = "art.schema.renameSpace";
        String SCHEMA_DROP_SPACE = "art.schema.dropSpace";
        String SCHEMA_INDICIES = "art.schema.indicies";
        String SCHEMA_FORMAT = "art.schema.format";

    }

    interface SelectOptions {
        String LIMIT = "limit";
        String OFFSET = "offset";
        String FILTER = "filter";
        String SORT = "sort";
        String DISTINCT = "distinct";
    }

    interface SortOptions {
        String COMPARATOR_MORE = "more";
        String COMPARATOR_LESS = "less";
    }

    interface FilterOptions {
        String OPERATOR_EQUALS = "equals";
        String OPERATOR_NOT_EQUALS = "notEquals";
        String OPERATOR_MORE = "more";
        String OPERATOR_LESS = "less";
        String OPERATOR_IN = "in";
        String OPERATOR_NOT_IN = "notIn";
        String OPERATOR_LIKE = "like";
        String OPERATOR_STARTS_WITH = "startsWith";
        String OPERATOR_ENDS_WITH = "endsWith";
        String OPERATOR_CONTAINS = "contains";
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
        String ID = "id";
        String ENGINE = "engine";
        String FIELD_COUNT = "field_count";
        String IF_NOT_EXISTS = "if_not_exists";
        String IS_LOCAL = "is_local";
        String IS_SYNC = "is_sync";
        String USER = "user";
        String TEMPORARY = "temporary";
        String FORMAT = "format";
    }

    interface IndexFields {
        String ID = "id";
        String TYPE = "type";
        String UNIQUE = "unique";
        String PARTS = "parts";
        String DIMENSION = "dimension";
        String DISTANCE = "distance";
        String BLOOM_FPR = "bloom_fpr";
        String PAGE_SIZE = "page_size";
        String RANGE_SIZE = "range_size";
        String RUN_COUNT_PER_LEVEL = "run_count_per_level";
        String RUN_SIZE_RATIO = "run_size_ratio";
        String SEQUENCE = "sequence";
        String FUNC = "func";
        String HINT = "hint";
        String IF_NOT_EXISTS = "if_not_exists";
    }

    interface IndexPartFields {
        String FIELD = "field";
        String TYPE = "type";
        String IS_NULLABLE = "is_nullable";
        String PATH = "path";
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
    enum TarantoolOperator {
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
