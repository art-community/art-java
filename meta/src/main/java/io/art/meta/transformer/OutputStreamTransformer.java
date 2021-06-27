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

import io.art.meta.exception.*;
import lombok.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static lombok.AccessLevel.*;
import java.io.*;

@NoArgsConstructor(access = PRIVATE)
public class OutputStreamTransformer implements MetaTransformer<OutputStream> {

    @Override
    public OutputStream fromByteArray(byte[] value) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(value.length);
        ignoreException(() -> outputStream.write(value), TransformationException::new);
        return outputStream;
    }

    public static OutputStreamTransformer OUTPUT_STREAM_TRANSFORMER = new OutputStreamTransformer();
}
