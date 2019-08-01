package ru.art.state.api.constants;

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
            String GET_CLUSTER_PROFILE_PATH = "/getClusterProfile";
            String CONNECT_PATH = "/connect";
            String INCREMENT_SESSION_PATH = "/sessions/increment";
            String DECREMENT_SESSION_PATH = "/sessions/decrement";
            String GET_SESSIONS_PATH = "/sessions";
            String GET_PROFILES_PATH = "/profiles";
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
            String LOCK_PATH = "/lock";
            String UNLOCK_PATH = "/unlock";
            String GET_CURRENT_LOCKS_PATH = "/getCurrentLocks";
        }
    }
}
