/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.rsocket.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.TCP;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;

@Getter
@Accessors(fluent = true)
@Builder(toBuilder = true, builderMethodName = "rsocketCommunicationTarget")
public class RsocketCommunicationTargetConfiguration {
    @Setter
    private String host;
    @Setter
    private Integer tcpPort;
    @Setter
    private Integer webSocketPort;
    @Builder.Default
    private final RsocketTransport transport = TCP;
    @Builder.Default
    private final RsocketDataFormat dataFormat = rsocketModule().getDefaultDataFormat();
    @Builder.Default
    private final boolean resumable = true;
}
