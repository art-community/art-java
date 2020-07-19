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

package io.art.server.model;

import lombok.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.*;
import static java.text.MessageFormat.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceMethodCommand {
    private String serviceId;
    private String methodId;

    public static ServiceMethodCommand parseServiceCommand(String command) {
        String[] commandStringSlitted = command.split(DOT);
        if (commandStringSlitted.length != 2) {
            throw new IllegalArgumentException(format(SERVICE_COMMAND_HAS_WRONG_FORMAT, command));
        }
        String serviceId = commandStringSlitted[0];
        String methodIdWithBraces = commandStringSlitted[1];
        return new ServiceMethodCommand(serviceId, methodIdWithBraces.replaceAll(SERVICE_COMMAND_REGEX, EMPTY_STRING));
    }

    @Override
    public String toString() {
        return serviceId + DOT + methodId + BRACKETS;
    }
}
