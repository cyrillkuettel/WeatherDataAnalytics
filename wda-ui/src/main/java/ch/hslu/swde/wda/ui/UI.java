package ch.hslu.swde.wda.ui;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Main Loop for Console Application
 */
public final class UI {
    private static final Logger Log = LogManager.getLogger(UI.class);

    /* Menu Constants */

    private final String CITY_MENU;
    private static final String WELCOME_MENU = "Hallo! Bitte loggen sie sich ein.";
    private static final String SELECT_TIMESPAN = "Zeitspanne angeben. Erwartetes Format ist \"dd-MM-yyyy - " +
            "dd-MM-yyyy\" Enter drücken für default Wert 27-11-2020 - 30-11-2020";
    private static final String TIMESPAN = "Zeitspanne festlegen [1]        gesamter Zeitraum [2]          " +
            "Beenden [0]";
    private static final String MENU_START = "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über " +
            "alle " + "Ortschaften laden [2] " + "     " +
            "Beenden " + "[0]";

    /* Start-Menu */
    private String currentMenu;


    private static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St. Gallen"};

    final static String DATE_FORMAT = "dd-MM-yyyy";

    public UI() {
         /* dynamically generated City List, it should be possible to add more
        cities */
        CITY_MENU = generateCityWithIndex(cities);
        execute();
    }


    public void execute() {


        showLogin();
        currentMenu = MENU_START;

        showActiveMenu();
        int selectedOption = eingabeEinlesen();
        if (selectedOption == 1) { /* Ony one city is asked*/

            currentMenu = CITY_MENU;
            showActiveMenu();
            selectedOption = eingabeEinlesen(); /* The city of interest*/
            String selectedCity = cities[selectedOption];

        } else if (selectedOption == 2) { /* Data of all cities */
            currentMenu = TIMESPAN;
            showActiveMenu();
            selectedOption = eingabeEinlesen();
           if (selectedOption == 1) {
                currentMenu = SELECT_TIMESPAN;
                 System.out.print("Beispiel: 12-25-2020 - 13-25-2020");
                showActiveMenu();
                String[] timeSpan = tryToParseTimeFrame();
                if (timeSpan.length > 1) {
                    Log.info("Parse Date successful");
                } else {
                    Log.info("Parse Date not successful");

                }
           } else  if (selectedOption == 2) { /* no constraints. All cities,  of all time */

           }
        }

    }

    /**
     * Try to parse the period of time. If successful, return both the Start and Enddate as String
     * If the function was not sucessful, it returns an empty String array of length 1.
     */
    private String[] tryToParseTimeFrame() {
        Scanner sc = new Scanner(System.in);



        String str = sc.nextLine();
        if (str == "") {
            Log.info("default value");
        }
        String[] dates = str.split(" - ");
        if (isValidDate(dates[0]) && isValidDate(dates[1])) {
            return dates;
        }
        return new String[0];
    }


    private void showActiveMenu() {
        System.out.println();
        System.out.println(currentMenu);
        System.out.print("Ihre Wahl: ");
    }

    /**
     * Read user input, which is a number for menu selection
     */
    private int eingabeEinlesen() {

        Scanner sc = new Scanner(System.in);
        int eingabe = -1;
        List<Integer> validMenuValues = new ArrayList<>();

        switch (currentMenu) {
            case MENU_START:
            case TIMESPAN:
                validMenuValues = Arrays.asList(1, 2, 0);
                break;
        }
        if (currentMenu.equals(CITY_MENU)) {
            List<Integer> validCityIndices = IntStream.rangeClosed(0, cities.length-1)
                    .boxed().collect(Collectors.toList());
            validMenuValues = validCityIndices;
        }


        do {
            try {
                eingabe = sc.nextInt();
                if (!validMenuValues.contains(eingabe)) {
                    showActiveMenu();
                }
            } catch (Exception e) {
                /* Clear the current Buffer */
                sc.nextLine();
                showActiveMenu();
            }

        } while (!validMenuValues.contains(eingabe));

        return eingabe;
    }

    public static boolean isValidDate(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private List<String> showLogin() {
        System.out.println(WELCOME_MENU);
        List<String> list = new ArrayList<>();

        Scanner sc = new Scanner(System.in);

        try {
            System.out.println();
            System.out.print("Benutzername: ");
            String name = sc.nextLine();
            System.out.print("Passwort: ");
            String vorname = sc.nextLine();
            list.add(name);
            list.add(vorname);

        } catch (Exception e) {
            Log.error("Fehler beim Lesen der Login Daten", e);
            System.err.println("\n Fehler beim Lesen der Login Daten");

        }

        return list;
    }

    /**
     * For each city, display it with corresponding number
     * @param cities
     */
    public final String generateCityWithIndex(final String[] cities) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < cities.length; i++) {
            line.append(i).append("  ").append(cities[i]).append("\n");
        }
        line.append("\n Bitte den Index der gewünschten Ortschaft eingeben.");
        return line.toString();
    }



}

