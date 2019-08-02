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

package ru.art.example.state;

import ru.art.core.module.ModuleState;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * State is needed for store some static constants,
 * which are needed in service
 * <p>
 * In this example state is number of incoming requests
 */

public class ExampleModuleState implements ModuleState {
    private final AtomicInteger requests = new AtomicInteger();

    public int getRequests() {
        return requests.get();
    }

    public ExampleModuleState setRequests(int requests) {
        this.requests.set(requests);
        return this;
    }

    public int incrementRequests() {
        return requests.incrementAndGet();
    }
}
