package ch.hslu.swde.wda.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class demoWeatherdataDownloader {
    private static final Logger Log = LogManager.getLogger(demoWeatherdataDownloader.class);

    public static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St. Gallen"};


    static List<String> logs = new ArrayList<>();

    public static void main(String[] args) {
        final WeatherdataDownloader weatherdatadownloader = new WeatherdataDownloader();

        weatherdatadownloader.startDownloadForCity("Zug");

        // Download all Data. This runs only one time
/*
        for (String city : cities ) {
            weatherdatadownloader.startDownloadForCity(city);
            logs.add(String.format("Downloaded city %s", city));
        }

 */



        logs.forEach(System.out::println);
    }


}


