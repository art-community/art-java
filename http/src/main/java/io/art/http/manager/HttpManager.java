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

package io.art.http.manager;


import io.art.communicator.action.*;
import io.art.http.configuration.*;
import io.art.http.refresher.*;
import io.art.http.server.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import lombok.*;
import reactor.core.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static lombok.AccessLevel.*;

public class HttpManager {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = Logging.logger(HttpManager.class);

    private final HttpModuleConfiguration configuration;
    private final HttpServer server;

    public HttpManager(HttpModuleRefresher refresher, HttpModuleConfiguration configuration) {
        this.configuration = configuration;
        this.server = new HttpServer(refresher, configuration);
    }

    public void initializeCommunicators() {
        configuration.getCommunicator()
                .getConnectors()
                .actions()
                .forEach(CommunicatorAction::initialize);
    }

    public void disposeCommunicators() {
        configuration.getCommunicator()
                .getConnectors()
                .actions()
                .forEach(CommunicatorAction::dispose);
    }


    public void initializeServer() {
        server.initialize();
    }

    public void disposeServer() {
        server.dispose();
    }

    public static void disposeHttp(Disposable http) {
        if (http.isDisposed()) {
            return;
        }
        ignoreException(http::dispose);
    }
}
