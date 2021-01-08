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

package io.art.rsocket.manager;


import io.art.communicator.configuration.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.server.*;
import io.art.rsocket.state.*;
import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketProtocol.*;
import static io.art.rsocket.module.RsocketModule.*;
import static lombok.AccessLevel.*;

@UtilityClass
public class RsocketManager {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketManager.class);

    @Getter(lazy = true, value = PRIVATE)
    private static final CommunicatorModuleConfiguration communicatorConfiguration = communicatorModule().configuration();

    @Getter(lazy = true, value = PRIVATE)
    private static final RsocketServer server = new RsocketServer(rsocketModule().configuration().getServerConfiguration());

    @Getter(lazy = true, value = PRIVATE)
    private static final RsocketModuleState state = rsocketModule().state();

    public void startConnectors() {
        getCommunicatorConfiguration()
                .getRegistry()
                .<RsocketCommunicator>getByProtocol(RSOCKET)
                .values()
                .forEach(proxy -> proxy.getImplementations().forEach(RsocketCommunicator::start));
    }

    public void stopConnectors() {
        getCommunicatorConfiguration()
                .getRegistry()
                .<RsocketCommunicator>getByProtocol(RSOCKET)
                .values()
                .forEach(proxy -> proxy.getImplementations().forEach(RsocketCommunicator::stop));
    }

    public void startServer() {
        getServer().start();
    }

    public void stopServer() {
        apply(getServer(), RsocketServer::stop);
    }


    public void disposeRsocket(Disposable rsocket) {
        if (rsocket.isDisposed()) {
            return;
        }
        getLogger().info(RSOCKET_DISPOSING);
        ignoreException(rsocket::dispose, getLogger()::error);
    }
}

