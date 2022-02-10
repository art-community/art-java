/*
 *    Copyright 2020 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a recursiveCopy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.art.core.converter;

import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.determiner.SystemDeterminer.*;

@UtilityClass
public class WslPathConverter {
    public static String convertToWslPath(String path) {
        if (!isWindows()) return path;
        if (isEmpty(path)) return path;
        if (SLASH.equals(EMPTY_STRING + path.charAt(0)) || BACKWARD_SLASH.equals(EMPTY_STRING + path.charAt(0))) {
            path = path.substring(1);
        }
        if (path.contains(WINDOWS_DISK_PATH_SLASH) || path.contains(WINDOWS_DISK_PATH_BACKWARD_SLASH)) {
            path = path
                    .replaceAll(WINDOWS_DISK_PATH_SLASH, SLASH)
                    .replaceAll(WINDOWS_DISK_PATH_BACKWARD_SLASH_REGEX, SLASH)
                    .replaceAll(BACKWARD_SLASH_REGEX, SLASH);
            String firstLetter = EMPTY_STRING + path.charAt(0);
            return WSL_DISK_PREFIX + firstLetter.toLowerCase() + path.substring(1);
        }
        return path;
    }
}
