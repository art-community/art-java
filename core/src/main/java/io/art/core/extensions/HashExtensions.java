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

package io.art.core.extensions;

import lombok.experimental.*;
import net.jpountz.xxhash.*;
import static io.art.core.constants.HashConstants.*;
import static net.jpountz.xxhash.XXHashFactory.*;

@UtilityClass
public class HashExtensions {
    private static final XXHash64 DEFAULT_HASH_64 = unsafeInstance().hash64();
    private static final XXHash32 DEFAULT_HASH_32 = unsafeInstance().hash32();

    public static long xx64(XXHash64 hash, byte[] content, long seed) {
        return hash.hash(content, 0, content.length, seed);
    }

    public static int xx32(XXHash32 hash, byte[] content, int seed) {
        return hash.hash(content, 0, content.length, seed);
    }


    public static long xx64(XXHash64 hash, byte[] content) {
        return xx64(hash, content, DEFAULT_XX64_HASH_SEED);
    }

    public static int xx32(XXHash32 hash, byte[] content) {
        return xx32(hash, content, DEFAULT_XX32_HASH_SEED);
    }


    public static long xx64(StreamingXXHash64 hash, byte[] content) {
        hash.update(content, 0, content.length);
        long value = hash.getValue();
        hash.reset();
        return value;
    }

    public static int xx32(StreamingXXHash32 hash, byte[] content) {
        hash.update(content, 0, content.length);
        int value = hash.getValue();
        hash.reset();
        return value;
    }


    public static long xx64(byte[] content, long seed) {
        return xx64(DEFAULT_HASH_64, content, seed);
    }

    public static int xx32(byte[] content, int seed) {
        return xx32(DEFAULT_HASH_32, content, seed);
    }


    public static long xx64(byte[] content) {
        return xx64(content, DEFAULT_XX64_HASH_SEED);
    }

    public static int xx32(byte[] content) {
        return xx32(content, DEFAULT_XX32_HASH_SEED);
    }
}
