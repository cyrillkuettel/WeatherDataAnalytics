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
    /**
     * Hardcoded cities. This is only for local testing.
     */
    private static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St. Gallen"};

    private static final String SELECT_TIMESPAN_END = "Enddatum: ";
    private final String SHOW_CITY_MENU;
    private static final String WELCOME_MENU = " Einloggen [1]      Neuer Benutzer erstellen [2] ";
    private static final String SELECT_TIMESPAN_START = "Startdatum angeben. Erwartetes Format ist \"dd.MM.yyyy\" \nEnter drücken für das default Datum.";
    private static final String TIMESPAN = "Zeitspanne festlegen [1]        gesamter Zeitraum [2]          " +
            "Beenden [0]";

    private static final String SELECT_DATA_OR_ALL_MENU = "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über " +
            "alle " + "Ortschaften laden [2] " + "     " +
            "Beenden " + "[0]";

    private String currentMenu;
    private static final String CREATE_NEW_USER_MENU = "Neuer Mitarbeiter.";


    final static String DATE_FORMAT = "dd-MM-yyyy";
    private static final String[] DEFAULT_DATE = {"27-11-2020", "30-11-2020"};
    private static int DEFAULT_DATE_STATE = -1;

    private static final String MIN_DATE_VALUE = "01-01-2020"; // minimum and maximum legal value, inclusive
    private static final String MAX_DATE_VALUE = "31-12-2020";


    public UI() {
         /* Generate City-List at runtime, the number of cities should not be static */
        SHOW_CITY_MENU = generateCityWithIndex(cities);

    }



    /**
     * starts a new User Interaction.
     */
    public void startFromBeginning() {
        int selectedOption;
        String selectedCity = "";
        String[] timePeriod = new String[2]; // index [0] ->  startDate
                                             // index [1] ->  endDate

        currentMenu = WELCOME_MENU;
        showActiveMenu();
        selectedOption = readOptionFromUser();
        if (selectedOption == 1) {  //  Einloggen
            String[] creds;
            int totalLoginAttempts = 0;
            do {
                creds = startLoginProcess(totalLoginAttempts);
                totalLoginAttempts++;
            } while(!isValidLogin(creds));

            if(isValidLogin(creds))  {
                System.out.println("Erfolgreich Eingeloggt!");
                currentMenu = SELECT_DATA_OR_ALL_MENU;
            }

        } else if (selectedOption == 2) { // neuen Benutzer erstellen
            currentMenu = CREATE_NEW_USER_MENU;
            Log.info("Erstelle neuer Benutzer");
        }


        showActiveMenu();
        selectedOption = readOptionFromUser();
        if (selectedOption == 1) { /* Ony one city is requested */

            currentMenu = SHOW_CITY_MENU;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            selectedCity = cities[selectedOption];
            timePeriod = getTimePeriod();

        } else if (selectedOption == 2) { /* All cities are considered */
            timePeriod = getTimePeriod();

        }

    }

    /**
     * Read user input, which is always a number. This number indicates the next action to take.
     * @return Number, which is the action the user takes.
     */
    public int readOptionFromUser() {

        Scanner sc = new Scanner(System.in);
        int eingabe = -1;
        List<Integer> validMenuValues = new ArrayList<>();

        switch (currentMenu) {
            case SELECT_DATA_OR_ALL_MENU:
            case TIMESPAN:
            case WELCOME_MENU:
                validMenuValues = Arrays.asList(1, 2, 0); // Almost always, there are 3 valid actions to choose for
                // each  menu
                break;
        }
        if (currentMenu.equals(SHOW_CITY_MENU)) {
            List<Integer> validCityIndices = IntStream.rangeClosed(0, cities.length - 1)
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


    /**
     * Asks the user to specify a Timespan. If the user does not want to restric search,
     * we simply select the largest possible value for the date interval.
     * @return Array with index 0 representing start- and index 1 representing the end date.
     */
    public String[] getTimePeriod() {
        String[] timeframe = new String[2];

        currentMenu = TIMESPAN;
        showActiveMenu();
        int selectedOption = readOptionFromUser();
        if (selectedOption == 1) {
            currentMenu = SELECT_TIMESPAN_START;
            showActiveMenu();
            String startDate = tryToParseDate();
            timeframe[0] = startDate;
            currentMenu = SELECT_TIMESPAN_END;
            showActiveMenu();
            String endDate = tryToParseDate();
            timeframe[1] = endDate;
        } else if (selectedOption == 2) { // No constraints. All cities / Of All time
            timeframe[0] = MIN_DATE_VALUE;
            timeframe[1] = MAX_DATE_VALUE;
        }
        return timeframe;
    }

    /**
     * Try to parse a single Date.
     */
    private String tryToParseDate() {
        Scanner sc = new Scanner(System.in);
        String input = " ";
        String date = "";

        do { // Loop until valid date
            if (!input.isEmpty()) {
                try {
                    input = sc.nextLine();
                    if (isValidDate(input)) {
                        date = input;
                    } else if (!input.isEmpty()){
                        System.out.println("Anderes Datumformat erwartet!");
                    }
                } catch (Exception e) {
                    /* Clear the current Buffer */
                    sc.nextLine();
                    showActiveMenu();
                }
            } else { // On Enter key: just use the default date
                System.out.println("Benutze das Standard Datum.");
                DEFAULT_DATE_STATE++;
                date = DEFAULT_DATE[DEFAULT_DATE_STATE];
            }
        } while (Objects.equals(date, ""));
        return date;
    }


    public void showActiveMenu() {
        System.out.println();
        System.out.println(currentMenu);
        System.out.print("Ihre Wahl: ");
    }



    /**
     * Expects a Date as String,  for example "27.11.2020"
     * @return True if the the input String is a valid Date, according to the specificed DATA_FORMAT
     */
    public static boolean isValidDate(String date) {
        if (date.contains(".")) {
            date = date.replaceAll("\\.", "-");
        }
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Determine if login is successful.
     * @return true if the login is a correct username / password combination, false if not.
     */
    private boolean isValidLogin(String[] checkThisLogin) {
        // only for testing ( this will change as soon as we can validate logins on
        // the server-side)
        return checkThisLogin[0] != " ";
    }

    /**
     * Ask the user to type in username and password. Then for this data the user provides, do some simple client-side
     * validation, to prevent sendingn malformed data to our server: The username and passsword should not exceed a
     * certain length.
     * @return String Array containing the username [index: 0] and password. [index: 1]
     */
   private String[] startLoginProcess(int attemptCount) {
       if (attemptCount > 0) {
           System.out.print("Ist nicht eine korrekte Benutzername / Password Kombination. Versuchen Sie es noch " +
                                    "einmal.");
       }
       String[] creds = new String[2];
       Scanner sc = new Scanner(System.in);
       do {
           try {
               System.out.println();
               System.out.print("Benutzername: ");
               String Benutzername = sc.nextLine();
               System.out.print("Passwort: ");
               // TODO: simple client-slide validation of this login data
               String Passwort = sc.nextLine();
               creds[0] = Benutzername;
               creds[1] = Passwort;
           } catch (Exception e) {
               Log.error("Fehler beim Lesen der Login Daten in startLoginProcess", e);
               System.err.println("\n Fehler beim Lesen der Login Daten");
           }
       } while (creds[0] == null && creds[1] == null);
       return creds;
   }

    /**
     * For each city, write it's name and coresponding index.
     * We then dislay this String on the Cosole, so that a sinle city can be selected by user.
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

