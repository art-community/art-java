/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.state.api.constants;

import static ru.art.http.constants.HttpCommonConstants.*;

public interface StateApiConstants {
    interface NetworkServiceConstants {
        String NETWORK_SERVICE_ID = "NETWORK_SERVICE";
        String NETWORK_COMMUNICATION_SERVICE_ID = "NETWORK_COMMUNICATION_SERVICE";

        interface Methods {
            String GET_CLUSTER_PROFILE = "GET_CLUSTER_PROFILE";
            String CONNECT = "CONNECT";
            String INCREMENT_SESSION = "INCREMENT_SESSION";
            String DECREMENT_SESSION = "DECREMENT_SESSION";
            String GET_SESSIONS = "GET_SESSIONS";
            String GET_PROFILES = "GET_PROFILES";
        }

        interface Paths {
            String GET_CLUSTER_PROFILE_PATH = API_PATH + "/getClusterProfile";
            String CONNECT_PATH = API_PATH + "/connect";
            String INCREMENT_SESSION_PATH = API_PATH + "/sessions/increment";
            String DECREMENT_SESSION_PATH = API_PATH + "/sessions/decrement";
            String GET_SESSIONS_PATH = API_PATH + "/sessions";
            String GET_PROFILES_PATH = API_PATH + "/profiles";
        }

        interface PathParameters {
            String PROFILE = "profile";
        }
    }

    interface LockServiceConstants {
        String LOCK_SERVICE_ID = "LOCK_SERVICE";
        String DEFAULT_LOCK = "DEFAULT_LOCK";

        interface Methods {
            String LOCK = "LOCK";
            String UNLOCK = "UNLOCK";
            String GET_CURRENT_LOCKS = "GET_CURRENT_LOCKS";
        }

        interface Paths {
            String LOCK_PATH = API_PATH + "/lock";
            String UNLOCK_PATH = API_PATH + "/unlock";
            String GET_CURRENT_LOCKS_PATH = API_PATH + "/getCurrentLocks";
        }
    }
}
