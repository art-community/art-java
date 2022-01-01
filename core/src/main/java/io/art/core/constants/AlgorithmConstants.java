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

package io.art.core.constants;

import io.art.core.exception.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.handler.ExceptionHandler.*;
import java.security.*;

public interface AlgorithmConstants {
    String MD5 = "MD5";

    MessageDigest SHA1_DIGEST = cast(wrapException(InternalRuntimeException::new).call(() -> MessageDigest.getInstance("SHA-1")));
}
