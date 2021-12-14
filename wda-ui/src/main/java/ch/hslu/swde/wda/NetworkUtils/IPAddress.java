package ch.hslu.swde.wda.NetworkUtils;

import java.net.SocketException;

public class IPAddress {
    public static void main(String[] args) throws SocketException {
        Utils networkUtils = new Utils();
        System.out.println(networkUtils.getIPAdress());
    }
}
