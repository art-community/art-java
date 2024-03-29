/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.manager;


import io.art.communicator.action.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.refresher.*;
import io.art.rsocket.server.*;
import reactor.core.*;
import static io.art.core.wrapper.ExceptionWrapper.*;

public class RsocketManager {
    private final RsocketServer server;
    private final RsocketModuleConfiguration configuration;

    public RsocketManager(RsocketModuleRefresher refresher, RsocketModuleConfiguration configuration) {
        this.configuration = configuration;
        this.server = new RsocketServer(refresher, configuration);
    }

    public void initializeCommunicators() {
        configuration.getCommunicator()
                .getCommunicators()
                .actions()
                .forEach(CommunicatorAction::initialize);
    }

    public void disposeCommunicators() {
        configuration.getCommunicator()
                .getCommunicators()
                .actions()
                .forEach(CommunicatorAction::dispose);
    }

    public void initializeServers() {
        server.initialize();
    }

    public void disposeServers() {
        server.dispose();
    }

    public static void disposeRsocket(Disposable rsocket) {
        if (rsocket.isDisposed()) return;
        ignoreException(rsocket::dispose);
    }
}
