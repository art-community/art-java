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

package io.art.core.extension;

import lombok.experimental.*;
import static java.lang.System.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;

@UtilityClass
public class ArraysExtensions {
    public static <T> T[] concatArrays(T[] a1, T[] a2) {
        if (isNull(a1) || a1.length == 0) return a2;
        if (isNull(a2) || a2.length == 0) return a1;
        T[] res = copyOf(a1, a1.length + a2.length);
        arraycopy(a2, 0, res, a1.length, a2.length);
        return res;
    }
}
