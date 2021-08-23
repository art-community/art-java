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

package io.art.core.constants;

import io.art.core.collection.*;
import io.art.core.factory.*;

public interface NetworkConstants {
    String BROADCAST_IP_ADDRESS = "0.0.0.0";
    String LOCALHOST_IP_ADDRESS = "127.0.0.1";
    int PORT_RANGE_MIN = 8000;
    int PORT_RANGE_MAX = 65535;
    int PORT_OFFSET = 100;
    String LOCALHOST = "localhost";
    ImmutableSet<String> DEFAULT_DNS_SERVERS = SetFactory.immutableSetOf(
            "8.8.8.8",
            "8.8.4.4",
            "1.1.1.1"
    );
}
