package ch.hslu.swde.wda.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class demoWeatherdataDownloader{
    private static final Logger Log = LogManager.getLogger(demoWeatherdataDownloader.class);

    public static void main(String[] args) {

        WeatherdataDownloader wd = new  WeatherdataDownloader();
        String cities = wd.getCitiesAsXML();
        System.out.println(cities);
    }
}
