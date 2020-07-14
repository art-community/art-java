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

package io.art.http.server.path;

import lombok.*;
import io.art.core.constants.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.CharConstants.SLASH;
import static io.art.core.constants.StringConstants.COLON;
import static io.art.core.constants.StringConstants.*;
import java.util.*;


@Getter
@Builder
public class HttpPath {
    private final String contextPath;
    @Singular("parameter")
    private final Set<String> parameters;

    @Override
    public String toString() {
        return contextPath + buildPathParams();
    }

    private String buildPathParams() {
        if (isEmpty(parameters)) return EMPTY_STRING;
        StringBuilder path = new StringBuilder(StringConstants.SLASH);
        Iterator<String> parametersIt = parameters.iterator();
        while (parametersIt.hasNext()) {
            path.append(COLON).append(parametersIt.next());
            if (parametersIt.hasNext()) {
                path.append(SLASH);
            }
        }
        return path.toString();
    }
}
