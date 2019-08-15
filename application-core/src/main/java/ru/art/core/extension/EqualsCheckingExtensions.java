/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.extension;

import java.util.Objects;

public interface EqualsCheckingExtensions {
    static <T> T ifEquals(T val, T pattern, T ifEquals) {
        return Objects.equals(val, pattern) ? ifEquals : val;
    }

    static <T> T ifNotEquals(T val, T pattern, T ifNotEquals) {
        return !Objects.equals(val, pattern) ? ifNotEquals : val;
    }
}
