package ch.hslu.swde.wda.NetworkUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    private static final Logger Log = LogManager.getLogger(Utils.class);

    /**
     * Returns the IP Address of the client within the hslu networks.
     * It is assumed that the address starst starts with "10."
     * @return IP Adress. If the Network can't be reached, it returns an empty String.
     */
    public String getIPAdress()  {

    String pattern= "10."; // filter the Adresses,
        List<String> listAdresses = getInetAddressFromNetworkInterface().stream()
                .map(element -> element.getHostAddress())
                .filter(address -> address.startsWith(pattern))
                .collect(Collectors.toList());
        if (!listAdresses.isEmpty()) {
            return listAdresses.get(0);
        }
        Log.warn("List of addresses is empty");
        return "";
    }
    public static List<InetAddress> getInetAddressFromNetworkInterface() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
                    .flatMap(ni -> Collections.list(ni.getInetAddresses()).stream())
                    .collect(Collectors.toList());
        } catch (SocketException e) {
            Log.warn("Something failed in NetworkInterface.getNetworkInterfaces");
            throw new IllegalStateException("Can not retrieve network interfaces", e);
        }
    }




    /**
     * Pings a HTTP URL, by sending a HTTP request.
     * @param url The HTTP URL to be pinged.
     * @param timeoutMillis The timeout in millis for both the connection timeout and the response read timeout. Note that
     * the total timeout is effectively two times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */
    public static boolean pingURL(String url, int timeoutMillis) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeoutMillis);
            connection.setReadTimeout(timeoutMillis);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }
}
