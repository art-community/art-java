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

package io.art.core.network.selector;

import lombok.experimental.*;
import static java.lang.String.*;
import static java.lang.System.*;
import static java.net.InetAddress.*;
import static javax.net.ServerSocketFactory.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.core.network.selector.PortSelector.SocketType.*;
import java.net.*;
import java.util.*;

@UtilityClass
public class PortSelector {
    private final static Random RANDOM = new Random(currentTimeMillis());

    public static int findAvailableTcpPort() {
        return findAvailableTcpPort(PORT_RANGE_MIN);
    }

    public static int findAvailableTcpPort(int minPort) {
        return findAvailableTcpPort(minPort, PORT_RANGE_MAX);
    }

    public static int findAvailableTcpPort(int minPort, int maxPort) {
        return TCP.findAvailablePort(minPort, maxPort);
    }

    public static Set<Integer> findAvailableTcpPorts(int numRequested) {
        return findAvailableTcpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    public static Set<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort) {
        return TCP.findAvailablePorts(numRequested, minPort, maxPort);
    }

    public static int findAvailableUdpPort() {
        return findAvailableUdpPort(PORT_RANGE_MIN);
    }

    public static int findAvailableUdpPort(int minPort) {
        return findAvailableUdpPort(minPort, PORT_RANGE_MAX);
    }

    public static int findAvailableUdpPort(int minPort, int maxPort) {
        return UDP.findAvailablePort(minPort, maxPort);
    }

    public static Set<Integer> findAvailableUdpPorts(int numRequested) {
        return findAvailableUdpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    public static Set<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort) {
        return UDP.findAvailablePorts(numRequested, minPort, maxPort);
    }

    enum SocketType {
        TCP {
            @Override
            protected boolean isPortAvailable(int port) {
                try {
                    getDefault()
                            .createServerSocket(port, 1, getByName(LOCALHOST))
                            .close();
                    return true;
                } catch (Throwable ex) {
                    return false;
                }
            }
        },

        UDP {
            @Override
            protected boolean isPortAvailable(int port) {
                try {
                    new DatagramSocket(port, getByName(LOCALHOST)).close();
                    return true;
                } catch (Throwable ex) {
                    return false;
                }
            }
        };

        protected abstract boolean isPortAvailable(int port);

        private int findRandomPort(int minPort, int maxPort) {
            int portRange = maxPort - minPort;
            return minPort + RANDOM.nextInt(portRange + 1);
        }

        int findAvailablePort(int minPort, int maxPort) {
            int portRange = maxPort - minPort;
            int candidatePort;
            int searchCounter = 0;
            do {
                if (searchCounter > portRange) {
                    throw new IllegalStateException(format(COULD_NOT_FIND_AVAILABLE_PORT_AFTER_ATTEMPTS, name(), minPort, maxPort, searchCounter));
                }
                candidatePort = findRandomPort(minPort, maxPort);
                searchCounter++;
            }
            while (!isPortAvailable(candidatePort));

            return candidatePort;
        }

        Set<Integer> findAvailablePorts(int numRequested, int minPort, int maxPort) {
            Set<Integer> availablePorts = treeOf();
            int attemptCount = 0;

            while ((++attemptCount <= numRequested + PORT_OFFSET) && availablePorts.size() < numRequested) {
                availablePorts.add(findAvailablePort(minPort, maxPort));
            }

            if (availablePorts.size() != numRequested) {
                throw new IllegalStateException(format(COULD_NOT_FIND_AVAILABLE_PORTS_IN_THE_RANGE, numRequested, name(), minPort, maxPort));
            }

            return availablePorts;
        }
    }
}
