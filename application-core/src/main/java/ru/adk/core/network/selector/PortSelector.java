package ru.adk.core.network.selector;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.net.InetAddress.getByName;
import static javax.net.ServerSocketFactory.getDefault;
import static ru.adk.core.constants.ExceptionMessages.COULD_NOT_FIND_AVAILABLE_PORTS_IN_THE_RANGE;
import static ru.adk.core.constants.ExceptionMessages.COULD_NOT_FIND_AVAILABLE_PORT_AFTER_ATTEMPTS;
import static ru.adk.core.constants.NetworkConstants.*;
import static ru.adk.core.factory.CollectionsFactory.treeOf;
import static ru.adk.core.network.selector.PortSelector.SocketType.TCP;
import static ru.adk.core.network.selector.PortSelector.SocketType.UDP;
import java.net.DatagramSocket;
import java.util.Random;
import java.util.Set;

public interface PortSelector {
    Random RANDOM = new Random(currentTimeMillis());

    static int findAvailableTcpPort() {
        return findAvailableTcpPort(PORT_RANGE_MIN);
    }

    static int findAvailableTcpPort(int minPort) {
        return findAvailableTcpPort(minPort, PORT_RANGE_MAX);
    }

    static int findAvailableTcpPort(int minPort, int maxPort) {
        return TCP.findAvailablePort(minPort, maxPort);
    }

    static Set<Integer> findAvailableTcpPorts(int numRequested) {
        return findAvailableTcpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    static Set<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort) {
        return TCP.findAvailablePorts(numRequested, minPort, maxPort);
    }

    static int findAvailableUdpPort() {
        return findAvailableUdpPort(PORT_RANGE_MIN);
    }

    static int findAvailableUdpPort(int minPort) {
        return findAvailableUdpPort(minPort, PORT_RANGE_MAX);
    }

    static int findAvailableUdpPort(int minPort, int maxPort) {
        return UDP.findAvailablePort(minPort, maxPort);
    }

    static Set<Integer> findAvailableUdpPorts(int numRequested) {
        return findAvailableUdpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    static Set<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort) {
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
                } catch (Exception ex) {
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
                } catch (Exception ex) {
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
