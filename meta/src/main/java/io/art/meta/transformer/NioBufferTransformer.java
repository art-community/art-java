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

import io.art.core.extensions.*;
import lombok.*;
import static lombok.AccessLevel.*;
import java.nio.*;

@NoArgsConstructor(access = PRIVATE)
public class NioBufferTransformer implements MetaTransformer<ByteBuffer> {

    @Override
    public byte[] toByteArray(ByteBuffer value) {
        return NioBufferExtensions.toByteArray(value);
    }

    @Override
    public ByteBuffer fromByteArray(byte[] value) {
        return NioBufferExtensions.from(value);
    }

    public static NioBufferTransformer NIO_BUFFER_TRANSFORMER = new NioBufferTransformer();
}
