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

package io.art.model.implementation.communicator;

import io.art.communicator.action.CommunicatorAction.*;
import io.art.core.collection.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.let;
import static io.art.core.collection.ImmutableMap.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class CommunicatorModuleModel {
    private final ImmutableMap<String, RsocketCommunicatorModel> rsocketCommunicators;

    public CommunicatorActionBuilder implement(String id, CommunicatorActionBuilder current) {
        return let(getCommunicators().get(id), communicator -> communicator.implement(current), current);
    }

    public ImmutableMap<String, CommunicatorModel> getCommunicators() {
        return rsocketCommunicators.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, Map.Entry::getValue));
    }
}
