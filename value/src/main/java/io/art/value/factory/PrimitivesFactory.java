/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.value.factory;

import io.art.value.immutable.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.*;
import static java.util.Objects.*;
import java.util.*;

public class PrimitivesFactory {
    public final static Primitive DEFAULT_STRING_PRIMITIVE = new Primitive(EMPTY_STRING, STRING);
    public final static Primitive DEFAULT_LONG_PRIMITIVE = new Primitive(0L, LONG);
    public final static Primitive DEFAULT_INT_PRIMITIVE = new Primitive(0, INT);
    public final static Primitive DEFAULT_BOOL_PRIMITIVE = new Primitive(false, BOOL);
    public final static Primitive DEFAULT_BYTE_PRIMITIVE = new Primitive(0, BYTE);
    public final static Primitive DEFAULT_DOUBLE_PRIMITIVE = new Primitive(0., DOUBLE);
    public final static Primitive DEFAULT_FLOAT_PRIMITIVE = new Primitive(0.f, FLOAT);

    private static final Primitive EMPTY_STRING_PRIMITIVE = new Primitive(null, STRING);
    private static final Primitive EMPTY_LONG_PRIMITIVE = new Primitive(null, LONG);
    private static final Primitive EMPTY_INT_PRIMITIVE = new Primitive(null, INT);
    private static final Primitive EMPTY_BOOL_PRIMITIVE = new Primitive(null, BOOL);
    private static final Primitive EMPTY_BYTE_PRIMITIVE = new Primitive(null, BYTE);
    private static final Primitive EMPTY_DOUBLE_PRIMITIVE = new Primitive(null, DOUBLE);
    private static final Primitive EMPTY_FLOAT_PRIMITIVE = new Primitive(null, FLOAT);

    private static final Map<Object, Primitive> CACHE = weakMap();

    public static Primitive stringPrimitive(String value) {
        if (isNull(value)) return EMPTY_STRING_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, STRING));
    }

    public static Primitive longPrimitive(Long value) {
        if (isNull(value)) return EMPTY_LONG_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, LONG));
    }

    public static Primitive intPrimitive(Integer value) {
        if (isNull(value)) return EMPTY_INT_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, INT));
    }

    public static Primitive shortPrimitive(Short value) {
        if (isNull(value)) return EMPTY_INT_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, INT));
    }

    public static Primitive charPrimitive(Character value) {
        if (isNull(value)) return EMPTY_STRING_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(let(value, character -> EMPTY_STRING + character, (String) null), STRING));
    }

    public static Primitive boolPrimitive(Boolean value) {
        if (isNull(value)) return EMPTY_BOOL_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, BOOL));
    }

    public static Primitive doublePrimitive(Double value) {
        if (isNull(value)) return EMPTY_DOUBLE_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, DOUBLE));
    }

    public static Primitive bytePrimitive(Byte value) {
        if (isNull(value)) return EMPTY_BYTE_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, BYTE));
    }

    public static Primitive floatPrimitive(Float value) {
        if (isNull(value)) return EMPTY_FLOAT_PRIMITIVE;
        return putIfAbsent(CACHE, value, () -> new Primitive(value, FLOAT));
    }
}
