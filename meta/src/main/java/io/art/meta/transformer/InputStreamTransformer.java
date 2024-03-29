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

package io.art.meta.transformer;

import io.art.core.extensions.*;
import lombok.*;
import static lombok.AccessLevel.*;
import java.io.*;

@NoArgsConstructor(access = PRIVATE)
public class InputStreamTransformer implements MetaTransformer<InputStream> {

    @Override
    public byte[] toByteArray(InputStream value) {
        return InputStreamExtensions.toByteArray(value);
    }

    @Override
    public InputStream fromByteArray(byte[] value) {
        return new ByteArrayInputStream(value);
    }

    public static InputStreamTransformer INPUT_STREAM_TRANSFORMER = new InputStreamTransformer();
}
