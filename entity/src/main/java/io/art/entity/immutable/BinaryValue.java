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

package io.art.entity.immutable;

import io.art.core.checker.*;
import io.art.entity.constants.*;
import lombok.*;
import static io.art.entity.constants.ValueType.*;

@Getter
@RequiredArgsConstructor
public class BinaryValue implements Value {
    private final byte[] content;
    private final ValueType type = BINARY;

    @Override
    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(content);
    }

    public static BinaryValue binary(byte[] array) {
        return new BinaryValue(array);
    }
}
