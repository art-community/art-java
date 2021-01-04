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

package io.art.core.model;

import io.art.core.constants.*;
import lombok.*;
import static io.art.core.constants.InterceptionAction.*;

@Getter
@Builder(builderMethodName = "interceptionResult")
public class InterceptionResult {
    @Builder.Default
    private final InterceptionAction action = NEXT;
    private final Object in;
    private final Object out;

    public static InterceptionResult next(Object in) {
        return interceptionResult().in(in).out(in).build();
    }

    public static InterceptionResult next(Object in, Object out) {
        return interceptionResult().in(in).out(out).build();
    }

    public static InterceptionResult process(Object in) {
        return interceptionResult()
                .in(in)
                .out(in)
                .action(PROCESS)
                .build();
    }

    public static InterceptionResult process(Object in, Object out) {
        return interceptionResult()
                .in(in)
                .action(PROCESS)
                .out(out)
                .build();
    }

    public static InterceptionResult terminate(Object in) {
        return interceptionResult()
                .in(in)
                .out(in)
                .action(TERMINATE)
                .build();
    }

    public static InterceptionResult terminate(Object in, Object out) {
        return interceptionResult()
                .in(in)
                .action(TERMINATE)
                .out(out)
                .build();
    }
}
