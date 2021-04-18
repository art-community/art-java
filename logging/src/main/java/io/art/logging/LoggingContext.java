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

package io.art.logging;

import lombok.experimental.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModuleConstants.LoggingParameters.*;
import static io.art.logging.ThreadContextExtensions.*;
import static org.apache.logging.log4j.ThreadContext.*;
import java.time.*;

@UtilityClass
public class LoggingContext {
    public static void putModuleLoggingContext() {
        putIfNotNull(LOG_TIMESTAMP, LocalDateTime.now().format(YYYY_MM_DD_T_HH_MM_SS_24H_SSS_DASH_FORMAT));
        putIfNotNull(MAIN_MODULE_ID_KEY, context().configuration().getMainModuleId());
        putIfNotNull(MODULES_KEY, context().getModuleNames());
        putIfNotNull(MODULE_JAR_KEY, context().configuration().getModuleJarName());
    }

    public static void putLoggingParameters(ServiceLoggingContext context) {
        putModuleLoggingContext();
        putIfNotNull(SERVICE_ID_KEY, context.getServiceId());
        putIfNotNull(SERVICE_METHOD_ID_KEY, context.getServiceMethodId());
        putIfNotNull(SERVICE_METHOD_COMMAND_KEY, context.getServiceMethodCommand());
        putIfNotNull(LOG_EVENT_TYPE, context.getLogEventType());
        putIfNotNull(REQUEST_START_TIME_KEY, LocalDateTime.now().format(YYYY_MM_DD_T_HH_MM_SS_24H_SSS_DASH_FORMAT));
        putIfNotNull(SERVICES_KEY, context.getLoadedServices());
    }

    public static void putLoggingContext(TransportLoggingContext context) {
        putModuleLoggingContext();
        putIfNotNull(PROTOCOL_KEY, context.getProtocol());
        putIfNotNull(TRACE_ID_KEY, context.getTraceId());
        putIfNotNull(ENVIRONMENT_KEY, context.getEnvironment());
        putIfNotNull(REQUEST_ID_KEY, context.getRequestId());
        putIfNotEmpty(PROFILE_KEY, context.getProfile());
    }

    public static void clearServiceLoggingContext() {
        remove(SERVICES_KEY);
        remove(REQUEST_KEY);
        remove(RESPONSE_KEY);
        remove(SERVICE_ID_KEY);
        remove(SERVICE_METHOD_ID_KEY);
        remove(SERVICE_METHOD_COMMAND_KEY);
        remove(REQUEST_START_TIME_KEY);
        remove(REQUEST_END_TIME_KEY);
        remove(EXECUTION_TIME_KEY);
        remove(SERVICE_EXCEPTION_KEY);
        remove(SERVICE_TYPES_KEY);
        remove(LOG_EVENT_TYPE);
    }

    public static void clearTransportLoggingContext() {
        remove(PROTOCOL_KEY);
        remove(TRACE_ID_KEY);
        remove(ENVIRONMENT_KEY);
        remove(REQUEST_ID_KEY);
        remove(PROFILE_KEY);
    }
}
