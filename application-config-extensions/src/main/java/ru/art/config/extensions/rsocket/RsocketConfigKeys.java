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

package ru.art.config.extensions.rsocket;

public interface RsocketConfigKeys {
    String RSOCKET_SECTION_ID = "rsocket";
    String RSOCKET_BALANCER_SECTION_ID = "rsocket.balancer";
    String RSOCKET_ACCEPTOR_SECTION_ID = "rsocket.acceptor";
    String RSOCKET_ACCEPTOR_TCP_PORT = "tcpPort";
    String RSOCKET_ACCEPTOR_WEB_SOCKET_PORT = "webSocketPort";
    String DEFAULT_DATA_FORMAT = "defaultDataFormat";
    String TRANSPORT = "transport";
}
