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

package ru.art.entity.interceptor;

import lombok.Builder;
import lombok.Getter;
import ru.art.core.constants.InterceptionStrategy;
import ru.art.entity.Value;
import static ru.art.core.constants.InterceptionStrategy.*;

@Getter
@Builder(builderMethodName = "valueInterceptionResult")
public class ValueInterceptionResult {
    @Builder.Default
    private final InterceptionStrategy nextInterceptionStrategy = NEXT_INTERCEPTOR;
    private final Value inValue;
    private Value outValue;

    public static  ValueInterceptionResult nextInterceptor(Value inValue) {
        return valueInterceptionResult().inValue(inValue).build();
    }

    public static  ValueInterceptionResult nextInterceptor(Value inValue, Value outValue) {
        return valueInterceptionResult().inValue(inValue).outValue(outValue).build();
    }

    public static  ValueInterceptionResult processHandling(Value inValue) {
        return valueInterceptionResult().inValue(inValue).nextInterceptionStrategy(PROCESS_HANDLING).build();
    }

    public static  ValueInterceptionResult processHandling(Value inValue, Value outValue) {
        return valueInterceptionResult().inValue(inValue).nextInterceptionStrategy(PROCESS_HANDLING).outValue(outValue).build();
    }

    public static  ValueInterceptionResult stopHandling(Value inValue) {
        return valueInterceptionResult().inValue(inValue).nextInterceptionStrategy(STOP_HANDLING).build();
    }

    public static  ValueInterceptionResult stopHandling(Value inValue, Value outValue) {
        return valueInterceptionResult().inValue(inValue).nextInterceptionStrategy(STOP_HANDLING).outValue(outValue).build();
    }
}
