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

package io.art.configurator.exception;

import static io.art.configurator.constants.ConfiguratorModuleConstants.Errors.*;
import static java.text.MessageFormat.*;

public class UnknownConfigurationFileExtensionException extends RuntimeException {
    public UnknownConfigurationFileExtensionException(String extension) {
        super(format(UNKNOWN_CONFIGURATION_SOURCE_FILE_EXTENSION, extension));
    }
}
