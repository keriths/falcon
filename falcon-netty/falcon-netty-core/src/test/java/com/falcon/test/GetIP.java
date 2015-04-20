package com.falcon.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by fanshuai on 15/4/18.
 */
public class GetIP {
    public static void main(String[] args){
        System.out.println(getFirstLocalIp());
    }

    public static String getFirstLocalIp() {
        List<String> allNoLoopbackAddresses = getAllLocalIp();
        if (allNoLoopbackAddresses.isEmpty()) {
            throw new IllegalStateException("Sorry, seems you don't have a network card :( ");
        }
        return allNoLoopbackAddresses.get(allNoLoopbackAddresses.size() - 1);
    }
    public static List<String> getAllLocalIp() {
        List<String> noLoopbackAddresses = new ArrayList<String>();
        List<InetAddress> allInetAddresses = getAllLocalAddress();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }
    public static List<InetAddress> getAllLocalAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            List<InetAddress> addresses = new ArrayList<InetAddress>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    System.out.println(inetAddress);
                    addresses.add(inetAddress);
                }
            }

            return addresses;
        } catch (SocketException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
