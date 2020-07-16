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

package io.art.entity.interceptor;

import lombok.*;
import io.art.core.constants.*;
import io.art.entity.immutable.Value;
import static io.art.core.constants.InterceptionStrategy.*;

@Getter
@Builder(builderMethodName = "valueInterceptionResult")
public class ValueInterceptionResult<InValue extends Value, OutValue extends Value> {
    @Builder.Default
    private final InterceptionStrategy nextInterceptionStrategy = NEXT_INTERCEPTOR;
    private final InValue inValue;
    private final OutValue outValue;

    public static <InValue extends Value> ValueInterceptionResult<InValue, InValue> nextInterceptor(InValue inValue) {
        return ValueInterceptionResult.<InValue, InValue>valueInterceptionResult().inValue(inValue).outValue(inValue).build();
    }

    public static <InValue extends Value, OutValue extends Value> ValueInterceptionResult<InValue, OutValue> nextInterceptor(InValue inValue, OutValue outValue) {
        return ValueInterceptionResult.<InValue, OutValue>valueInterceptionResult().inValue(inValue).outValue(outValue).build();
    }

    public static <InValue extends Value> ValueInterceptionResult<InValue, InValue> processHandling(InValue inValue) {
        return ValueInterceptionResult.<InValue, InValue>valueInterceptionResult()
                .inValue(inValue)
                .outValue(inValue)
                .nextInterceptionStrategy(PROCESS_HANDLING)
                .build();
    }

    public static <InValue extends Value, OutValue extends Value> ValueInterceptionResult<InValue, OutValue> processHandling(InValue inValue, OutValue outValue) {
        return ValueInterceptionResult.<InValue, OutValue>valueInterceptionResult()
                .inValue(inValue)
                .nextInterceptionStrategy(PROCESS_HANDLING)
                .outValue(outValue)
                .build();
    }

    public static <InValue extends Value> ValueInterceptionResult<InValue, InValue> stopHandling(InValue inValue) {
        return ValueInterceptionResult.<InValue, InValue>valueInterceptionResult()
                .inValue(inValue)
                .outValue(inValue)
                .nextInterceptionStrategy(STOP_HANDLING)
                .build();
    }

    public static <InValue extends Value, OutValue extends Value> ValueInterceptionResult<InValue, OutValue> stopHandling(InValue inValue, OutValue outValue) {
        return ValueInterceptionResult.<InValue, OutValue>valueInterceptionResult()
                .inValue(inValue)
                .nextInterceptionStrategy(STOP_HANDLING)
                .outValue(outValue)
                .build();
    }
}
