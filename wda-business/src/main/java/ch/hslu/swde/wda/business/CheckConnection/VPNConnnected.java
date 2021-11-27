package ch.hslu.swde.wda.business.CheckConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Check if we can ping the Server, which is only possible inside the HSLU network.
 */
public final class VPNConnnected {
    private static final Logger Log = LogManager.getLogger(VPNConnnected.class);
    static final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities";


    public static void main(String[] args) {
        if (Utils.pingURL(CITY_URL, 100000)) {
            Log.info("Connection established");
        } else {
            Log.warn("Could not ping swde.el.ee.intern:800. Are you connected to VPN?");
        }
    }

}
