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


import io.art.communicator.specification.*;
import io.art.rsocket.server.*;
import lombok.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketProtocol.*;
import static lombok.AccessLevel.*;

@UtilityClass
public class RsocketManager {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketManager.class);
    private static final RsocketServer SERVER = new RsocketServer();

    public void initializeCommunicators() {
        communicatorModule()
                .configuration()
                .getRegistry()
                .getByProtocol(RSOCKET)
                .values()
                .forEach(proxy -> proxy.getSpecifications().forEach(CommunicatorSpecification::initialize));
    }

    public void disposeCommunicators() {
        communicatorModule().configuration()
                .getRegistry()
                .getByProtocol(RSOCKET)
                .values()
                .forEach(proxy -> proxy.getSpecifications().forEach(CommunicatorSpecification::dispose));
    }

    public void initializeServer() {
        SERVER.initialize();
    }

    public void disposeServer() {
        SERVER.dispose();
    }


    public void disposeRsocket(Disposable rsocket) {
        if (rsocket.isDisposed()) {
            return;
        }
        getLogger().info(RSOCKET_DISPOSING);
        ignoreException(rsocket::dispose, getLogger()::error);
    }
}

