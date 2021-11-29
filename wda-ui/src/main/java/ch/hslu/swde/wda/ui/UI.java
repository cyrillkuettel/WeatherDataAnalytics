package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.CheckConnection.Utils;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.persister.DbHelper;
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

    private static final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata" +
            "/cities";
    private static final String SELECT_TIMESPAN_END = "Enddatum: ";
    private final String SHOW_CITY_MENU;
    private static final String PROMPT_USER_TO_LOGIN = "Bitte Loggen Sie sich ein.";
    private static final String ASK_PASSWORD = "Bitte Passwort eingeben: ";
    private static final String ASK_USERNAME = "Bitte Benutzername eingeben: ";

    private static final String CONFIRM_NEW_USER_CREATED = "Der neue Benutzer wurde erstellt";
    private static final String WELCOME_MENU = " Einloggen [1]      Neuer Benutzer erstellen [2] ";
    private static final String SELECT_TIMESPAN_START = "Startdatum angeben. Erwartetes Format ist \"dd.MM.yyyy\" \nEnter drücken für das default Datum.";
    private static final String TIMESPAN = "Zeitspanne festlegen [1]        gesamter Zeitraum [2]          " +
            "Beenden [0]";

    private static final String SELECT_DATA_OR_ALL_MENU = "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über " +
            "alle " + "Ortschaften laden [2] " + "     " +
            "Beenden " + "[0]";

    private String currentMenu;
    private static final String WARN_INVALID_LOGIN = "Ist nicht eine korrekte Benutzername / Password Kombination. " +
            "Versuchen Sie es noch einmal";
    private static final String WARN_LOGIN_VALIDATION_NOT_PASSED = "Das ist ein ungülties Passwort / Benutzername. " +
            "Passwort muss mindestens 8 Zeichen lang sein, Benutzername mindestens 3 Zeichen";

    final static String DATE_FORMAT = "dd-MM-yyyy";
    private static final String[] DEFAULT_DATE = {"27-11-2020", "30-11-2020"};
    private static int DEFAULT_DATE_STATE = -1;

    private static final String MIN_DATE_VALUE = "01-01-2020"; // minimum and maximum legal value, inclusive
    private static final String MAX_DATE_VALUE = "31-12-2020";
    static final int MAX_USERNAME_LEN = 40;
    static final int MIN_LENGTH_USERNAME = 3;
    static final int MIN_PASSWORD_LENGTH = 8;

    public UI() {
        if (Utils.pingURL(CITY_URL, 100000)) {
            Log.info("Connection established");
        } else {
            Log.error("Could not ping swde.el.ee.intern:80. Are you connected to VPN?");

        }


        // Load the cities from db when creating UI

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

        if (selectedOption == 2) { // create new user
            Log.info("Staring process to create a new User");

            String[] newCredentials = askForUsernamePassword(0);
            System.out.println(CONFIRM_NEW_USER_CREATED);
            // TODO:  write new User to Database
        }

        System.out.println(PROMPT_USER_TO_LOGIN);

        String[] creds;
        int totalLoginAttempts = 0;
        do {
            creds = askForUsernamePassword(totalLoginAttempts);
            totalLoginAttempts++;
        } while(!isValidLogin(creds));

        if(isValidLogin(creds))  {
            System.out.println("Erfolgreich Eingeloggt!");
            currentMenu = SELECT_DATA_OR_ALL_MENU;
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
     * Read user input: Numerical in all cases. The actual meaning of the number can be read from the currentMenu.
     * @return the action the user takes as a numerical value.
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
        if (currentMenu.equals(SHOW_CITY_MENU)) { // this just generates a range of numbers, as long as cities.length
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
     * @return Array with index 0 representing start, and index 1 representing the end date.
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
        // only for testing (this will change as soon as we can validate logins on
        // the server-side)
        return checkThisLogin[0] != " " && checkThisLogin[1].length() > 0 &&  checkThisLogin[0].length() > 0 ;
    }

    /**
     * Ask the user to type in username and password. This method can be used both in login and when creating a new
     * user. It will Loop indefinitely, until the condition in {@link #simpleLoginValidationPassed(String, String)
     * condition passes}
     * Then for this data the user provides, do some simple client-side
     * validation, to prevent sendingn malformed data to our server: The username and passsword should not exceed a
     * certain length.
     * @return String Array containing the username [index: 0] and password. [index: 1]
     */
   private String[] askForUsernamePassword(int attemptCount) {



       if (attemptCount > 0) {
           System.out.print(WARN_INVALID_LOGIN);
       }
       String[] creds = new String[2];
       Scanner sc = new Scanner(System.in);
       do {
           try {
               System.out.println();
               System.out.print(ASK_USERNAME);
               String username = sc.nextLine();
               System.out.print(ASK_PASSWORD);
               String password = sc.nextLine();
               Log.info("das passort" + password);
               if ( simpleLoginValidationPassed(username, password)) {
                   creds[0] = username;
                   creds[1] = password;
               } else {
                   System.out.println(WARN_LOGIN_VALIDATION_NOT_PASSED);
               }
           } catch (Exception e) {
               Log.error("Error while reading Username or Password in method askForUsernamePassword", e);
               System.err.println("\n Fehler beim Lesen der Login Daten");
           }
       } while (creds[0] == null && creds[1] == null);
       return creds;
   }

    /**
     * This Function already checks if the username and password pass certain basic validation.
     * @return true if the credidentals are valid, false if there is problem.
     */
    public boolean simpleLoginValidationPassed(String username, String password) {
        if ( username.length() < MIN_LENGTH_USERNAME || username.length() > MAX_USERNAME_LEN) {
            return false;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) return false;
        return true;
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


    public String[] getCitiesFromDatabase() {

        List<City> dbCities = DbHelper.selectAllCities();
        String[] cities = new String[dbCities.size()];
        Iterator<City> it = dbCities.iterator();
        int count = 0;
        while ( it.hasNext()) {
            cities[count] = it.next().getName();
        }

        return cities;
    }
}

