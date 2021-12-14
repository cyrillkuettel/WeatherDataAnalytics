package ch.hslu.swde.wda.NetworkUtils;

import ch.hslu.swde.wda.business.demoWeatherdataDownloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Check if we can ping the Server, which is only possible inside the HSLU network.
 */
public final class VPNConnnected {
    private static final Logger Log = LogManager.getLogger(VPNConnnected.class);
    static final String BASE_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/";


    public static void main(String[] args) {

        for (String city: demoWeatherdataDownloader.cities) {
            String testUrlConnection = BASE_URL;
            if (Utils.pingURL(testUrlConnection, 100000)) {
                Log.info("Connection established");
            } else {
                Log.warn(String.format("Could not ping %s Are you connected to VPN?", testUrlConnection));
            }
        }


    }

}
