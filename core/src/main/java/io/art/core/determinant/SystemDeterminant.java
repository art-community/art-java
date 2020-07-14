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

package io.art.core.determinant;

import lombok.experimental.*;
import static java.lang.System.*;
import static io.art.core.constants.SystemNamePatterns.*;
import static io.art.core.constants.SystemProperties.*;

@UtilityClass
public final class SystemDeterminant {
    private static final String OS = getProperty(OS_NAME_PROPERTY).toLowerCase();

    public static boolean isWindows() {
        return OS.contains(WIN);
    }

    public static boolean isMac() {
        return OS.contains(MAC);
    }

    public static boolean isUnix() {
        return OS.contains(NIX) || OS.contains(NUX) || OS.indexOf(AIX) > 0;
    }

    public static boolean isSolaris() {
        return OS.contains(SUNOS);
    }
}
