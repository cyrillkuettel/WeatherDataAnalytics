package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.CheckConnection.Utils;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.WeatherData;
import ch.hslu.swde.wda.persister.DbHelper;
import ch.hslu.swde.wda.persister.PersistCity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
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
    static final String DEFAULT_DATE_KEYWORD = "skip";
    private final String SHOW_CITY_MENU;
    private static final String PROMPT_USER_TO_LOGIN = "Bitte Loggen Sie sich ein.";
    private static final String ASK_PASSWORD = "Bitte Passwort eingeben: ";
    private static final String ASK_USERNAME = "Bitte Benutzername eingeben: ";

    private static final String CONFIRM_NEW_USER_CREATED = "Erstellung erfolgreich.";
    private static final String WELCOME_MENU = " Einloggen [1]      Neuer Benutzer erstellen [2] ";
    private static final String SELECT_TIMESPAN_START =
            "Startdatum angeben. Erwartetes Format ist \"dd.MM.yyyy\" \n\"" + DEFAULT_DATE_KEYWORD + "\" für das " +
                    "Standart " +
                    "Datum";
    private static final String TIMESPAN = "Zeitspanne festlegen [1]        gesamter Zeitraum [2]          " +
            "Beenden [0]";

    private static final String SELECT_DATA_OR_ALL_MENU =
            "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über " +
                    "alle " + "Ortschaften laden [2] " + "     " +
                    "Beenden " + "[0]";

    private String currentMenu;
    private static final String WARN_INVALID_LOGIN = "Ist nicht eine korrekte Benutzername / Password Kombination. " +
            "Versuchen Sie es noch einmal";
    private static final String WARN_LOGIN_VALIDATION_NOT_PASSED = "Das ist ein ungülties Passwort / Benutzername. " +
            "Passwort muss mindestens 8 Zeichen lang sein, Benutzername mindestens 3 Zeichen";

    final static String DATE_FORMAT = "dd-MM-yyyy";
    private static final String[] DEFAULT_DATE = {"29-11-2021", "30-11-2021"};

    // minimum and maximum valid value, inclusive
    static final String MIN_DATE_VALUE = "01-01-2020";
    static final String MAX_DATE_VALUE = "31-12-2020";
    static final int MAX_USERNAME_LEN = 40;
    static final int MIN_LENGTH_USERNAME = 3;
    static final int MIN_PASSWORD_LENGTH = 8;
    Scanner sc;

    public UI() {
        this.sc = new Scanner(System.in);
        if (Utils.pingURL(CITY_URL, 10000)) {
            Log.info("VPN connected!");
        } else {
            Log.error("Could not ping swde.el.ee.intern:80. Are you connected to https://vpn.hslu.ch?");

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
        String selectedCity;
        String[] selectedTimePeriod = new String[2]; // timePeriod[0] = startDate, timePeriod[1] = endDate


        currentMenu = WELCOME_MENU;
        showActiveMenu();
        selectedOption = readOptionFromUser();

        if (selectedOption == 2) { // create new user
            Log.info("Staring process to create a new User");

            String[] newCredentials = askForUsernamePassword(0);
            System.out.println(CONFIRM_NEW_USER_CREATED);
            // TODO:  write new User to Database
        }

        String[] creds;
        int totalLoginAttempts = 0;
        do {
            creds = askForUsernamePassword(totalLoginAttempts);
            totalLoginAttempts++;
        } while (!isValidLogin(creds));

        if (isValidLogin(creds)) {
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
            selectedTimePeriod = getTimePeriod();

        } else if (selectedOption == 2) { /* All cities are considered */
            selectedTimePeriod = getTimePeriod();

        }

    }

    /**
     * Read user input: Numerical in all cases. The actual meaning of the number can be read from the currentMenu.
     *
     * @return the action the user takes as a numerical value.
     */
    public int readOptionFromUser() {

        // Scanner sc = new Scanner(System.in);

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
                eingabe = Integer.parseInt(sc.next());
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
     * we simply select the largest possible value for the date interval in 2020.
     * If the startDate is not older than endDate, this just swaps them.
     *
     * @return Array with index 0 representing start, and index 1 representing the end date.
     */
    public String[] getTimePeriod() {
        String[] timeframe = new String[2];

        currentMenu = TIMESPAN;
        showActiveMenu();
        int selectedOption = readOptionFromUser();
        if (selectedOption == 1) {
            Log.info("You have decided to set a custom timePeriod");
            currentMenu = SELECT_TIMESPAN_START;
            showActiveMenu();
            String startDate = tryToParseDate();
            String endDate;
            if (!startDate.equals(DEFAULT_DATE[0])) { // no default date
                timeframe[0] = startDate;
                currentMenu = SELECT_TIMESPAN_END;
                showActiveMenu();
                endDate = tryToParseDate();
            } else {
                endDate = DEFAULT_DATE[1];
            }
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
    public String tryToParseDate() {

        String input = " ";
        String date = "";

        do {
            if (!input.isEmpty()) {
                try {
                    input = sc.next();
                    if (input.equals(DEFAULT_DATE_KEYWORD)) {
                        System.out.printf("Benutze das Standard Datum %s bis %s%n", DEFAULT_DATE[0],
                                          DEFAULT_DATE[1]);
                        // if (isValidDate(DEFAULT_DATE[0])) {
                        date = DEFAULT_DATE[0];
                        // }

                    } else {
                        if (isValidDate(input)) {
                            date = input;
                        } else {
                            System.out.println("Datumsformat nicht korrekt. Versuchen Sie es noch einmal");
                            showActiveMenu();
                        }
                    }
                } catch (Exception e) {
                    /* Clear the current Buffer */
                    sc.nextLine();
                    showActiveMenu();
                }
            }
        } while (Objects.equals(date, ""));
        return date;
    }

    public List<String> dummyScanner() {
        // Scanner sc = new Scanner(System.in);
        String input = "";
        List<String> parsedSTrings = new ArrayList<>();
        int count = 0;
        do {
            count++;
            try {
                input = sc.next();
            } catch (Exception e) {
                /* Clear the current Buffer */
                sc.next();
                showActiveMenu();
            }
            parsedSTrings.add(input);
        } while (sc.hasNext());
        Log.info(String.format("Looped %d times", count));
        return parsedSTrings;
    }


    public void showActiveMenu() {
        System.out.println();
        System.out.println(currentMenu);
        System.out.print("Ihre Wahl: ");
    }


    /**
     * Expects a Date as String. If the year is not specified, it will asssume 2020!
     * Accepted Formats:
     * "27.11.2020"
     * "27-11-2020"
     * 27.11.
     * 26-11-
     *
     * @return True if the the input String is a valid Date, according to the specificed DATA_FORMAT
     */
    public static boolean isValidDate(String date) {
        if (date.contains(".")) {  // unify format
            date = date.replaceAll("\\.", "-");
        }
        date = inferYearIfNotPresent(date);

        if (!date.contains("2020")) {
            Log.warn(String.format(" %s is is outside of the valid year range. Year should always be 2020", date));
            return false;
        }

        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);

            return true;
        } catch (ParseException e) {
            Log.warn(String.format("%s is not a valid date.", date));
            return false;
        }
    }

    /*
     * adds the year 2020 if not present.
     * Input should not contain dots.
     * It is assumed that this method is only called inside isValidDate!
     */
    public static String inferYearIfNotPresent(final String date) {
        final String deliminer = "\\-";

        String[] dates = date.split(deliminer);
        /*
        System.out.println(Arrays.toString(dates));
        System.out.println();
        System.exit(0);
        */

        String modifiedDateWithYear = "";
        if (dates.length == 2) {
            Log.info(String.format("date format without year detected. Date= %s", date));
            modifiedDateWithYear = date + "2020";
            return modifiedDateWithYear;
        }
        return date;
    }

    /**
     * Determine if login is successful.
     *
     * @return true if the login is a correct username / password combination, false if not.
     */
    private boolean isValidLogin(String[] checkThisLogin) {
        // only for testing (this will change as soon as we can validate logins on
        // the server-side)
        return checkThisLogin[0] != " " && checkThisLogin[1].length() > 0 && checkThisLogin[0].length() > 0;
    }

    /**
     * Ask the user to type in username and password. This method can be used both in login and when creating a new
     * user. It will Loop indefinitely, until the condition in {@link #simpleLoginValidationPassed(String, String)
     * condition passes}
     *
     * @return String Array containing the username [index: 0] and password. [index: 1]
     */
    public String[] askForUsernamePassword(int attemptCount) {

        if (attemptCount > 0) {
            System.out.print(WARN_INVALID_LOGIN);
        }
        String[] credentials = new String[2];
        // removed scanner
        do {
            try {
                System.out.print(ASK_USERNAME);
                String username = sc.next();
                System.out.print(ASK_PASSWORD);
                String password = sc.next();
                if (simpleLoginValidationPassed(username, password)) {
                    credentials[0] = username;
                    credentials[1] = password;
                } else {
                    System.out.println(WARN_LOGIN_VALIDATION_NOT_PASSED);
                }
            } catch (Exception e) {
                Log.error("Error while reading Username or Password in method askForUsernamePassword", e);
                System.err.println("\n Fehler beim Lesen der Login Daten");
            }
        } while (credentials[0] == null && credentials[1] == null);
        return credentials;
    }

    /**
     * This Function already checks if the username and password pass certain basic validations.
     * This is not a security measure, it just prevents sending malformed data to our server: For example: the username
     * cannot be an empty String.
     *
     * @return true if the credidentals are valid, false if there is problem.
     */
    public boolean simpleLoginValidationPassed(String username, String password) {
        if (username.length() < MIN_LENGTH_USERNAME || username.length() > MAX_USERNAME_LEN) {

            Log.info(String.format("Username Length should be at least %d and not longer than %d",
                                   MIN_LENGTH_USERNAME,
                                   MAX_USERNAME_LEN));
            return false;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            Log.info(String.format("The minimum Password length is %d ", MIN_PASSWORD_LENGTH));
            return false;
        }
        return true;
    }

    /**
     * For each city, write it's name and coresponding index.
     * We then dislay this String on the Cosole, so that a sinle city can be selected by user.
     *
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
        while (it.hasNext()) {
            cities[count] = it.next().getName();
        }

        return cities;
    }

    /**
     * Only for testing
     * copied from ch/hslu/swde/wda/persister/test.java
     */
    public void insertTestCities() {
        //int zipCode,String name, float longitude, float latitude
        City city1 = new City(4900, "Langenthal", 3.0f, 22.11f);
        City city2 = new City(4901, "Bern", 5.6f, 4.922f);
        City city3 = new City(8000, "Zurich", 2.7f, 5.9343f);

        List<City> cityList = new ArrayList();
        cityList.add(city1);
        cityList.add(city2);
        cityList.add(city3);
        PersistCity.insertCity(cityList);
    }

    public void selectWeatherByDateAndCity(String cityname, String start, String end) {

        start = "2020-12-30";
        end = "2021-11-28";

        java.sql.Date startDate = java.sql.Date.valueOf(start);
        java.sql.Date endDate = Date.valueOf(end);


        List<WeatherData> requestedWeatherData = DbHelper.selectWeatherDataSingleCity("Langenthal", startDate,
                                                                                      endDate);
    }


}

