/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.meta.transformer;


import lombok.*;
import lombok.experimental.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import java.util.*;

@Getter
@Accessors(fluent = true)
public class OutputTransformers {
    private final MetaTransformer<?> mapTransformer;
    private final MetaTransformer<?> arrayTransformer;
    private final MetaTransformer<?> stringTransformer;
    private final MetaTransformer<?> longTransformer;
    private final MetaTransformer<?> doubleTransformer;
    private final MetaTransformer<?> floatTransformer;
    private final MetaTransformer<?> integerTransformer;
    private final MetaTransformer<?> booleanTransformer;
    private final MetaTransformer<?> characterTransformer;
    private final MetaTransformer<?> shortTransformer;
    private final MetaTransformer<?> byteTransformer;
    private final MetaTransformer<?> binaryTransformer;

    public OutputTransformers(Map<MetaTypeExternalKind, MetaTransformer<?>> transformers) {
        mapTransformer = transformers.get(MAP);
        arrayTransformer = transformers.get(ARRAY);
        stringTransformer = transformers.get(STRING);
        longTransformer = transformers.get(LONG);
        doubleTransformer = transformers.get(DOUBLE);
        floatTransformer = transformers.get(FLOAT);
        integerTransformer = transformers.get(INTEGER);
        booleanTransformer = transformers.get(BOOLEAN);
        characterTransformer = transformers.get(CHARACTER);
        shortTransformer = transformers.get(SHORT);
        byteTransformer = transformers.get(BYTE);
        binaryTransformer = transformers.get(BINARY);
    }
}
