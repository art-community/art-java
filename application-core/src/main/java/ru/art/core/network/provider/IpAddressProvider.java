package ru.art.core.network.provider;

import static java.net.NetworkInterface.getNetworkInterfaces;
import static ru.art.core.constants.NetworkConstants.LOCALHOST_IP_ADDRESS;
import static ru.art.core.constants.StringConstants.IP_4_REGEX_PATTERN;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public interface IpAddressProvider {
    static String getIpAddress() {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = getNetworkInterfaces();
        } catch (SocketException e) {
            return LOCALHOST_IP_ADDRESS;
        }
        while (networkInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> addressEnumeration = networkInterfaces.nextElement().getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress inetAddress = addressEnumeration.nextElement();
                String currentAddress = inetAddress.getHostAddress();
                if (!inetAddress.isLoopbackAddress() && IP_4_REGEX_PATTERN.matcher(currentAddress).matches()) {
                    return currentAddress;
                }

            }
        }
        return LOCALHOST_IP_ADDRESS;
    }
}
