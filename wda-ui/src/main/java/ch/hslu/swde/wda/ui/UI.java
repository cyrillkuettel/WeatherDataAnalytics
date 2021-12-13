package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.CheckConnection.Utils;
import ch.hslu.swde.wda.GlobalConstants;
import ch.hslu.swde.wda.business.BusinessHandler;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ch.hslu.swde.wda.GlobalConstants.*;


/**
 * Main Loop for Console Application
 */
public final class UI {
    private static final Logger Log = LogManager.getLogger(UI.class);

    /**
     * Generated city names at Runtime
     */
    private String CITY_NAMES_WITH_INDEX_MENU = "not initialized";

    /**
     * The current active Menu.
     */
    private String currentMenu;
    private String[] availableCities;


    Scanner scanner;
    DatabaseOutputFormatter databaseOutputFormatter = new DatabaseOutputFormatter();

    public UI() {
        scanner = new Scanner(System.in);

        if (!Utils.pingURL(GlobalConstants.WEATHERDATA_PROVIDER, 10000)) {
            Log.fatal(ANSI_YELLOW + "Could not ping swde.el.ee.intern:80. Are you connected to https://vpn.hslu.ch?" + ANSI_RESET);
        }

    }

    public void loadCityNamesToMemory() {
        availableCities = databaseOutputFormatter.convertCitiesFromArrayToList();
        CITY_NAMES_WITH_INDEX_MENU = generateCityWithIndex(availableCities);
    }

    public void testRMI() {
        final String rmiServerIP = "10.155.228.206"; // change this
        final int rmiPort = 1099;

        final String projectDir = System.getProperty("user.dir"); // for example: /home/cyrill/Desktop/g07-wda
        final String clientPolicyRelativeDir = "/wda-ui/client.policy";
        final String policy = String.format("file:%s%s", projectDir, clientPolicyRelativeDir);

        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", policy);
            System.setSecurityManager(new SecurityManager());
        } else {
            Log.info("There is already an installed security manager. java.security.policy might not have been set");
        }

        final String url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + BusinessHandler.RO_NAME;

        BusinessHandler stub = null;
        try {
            stub = (BusinessHandler) Naming.lookup(url);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            Log.info("Naming.lookup did throw an Exception");
            e.printStackTrace();
        }

        List<String> list = null;
        try {
            list = stub.getCityNamesAsList();
            System.out.println(list);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * starts a new User Interaction.
     */
    public void startFromBeginning() {
        /* The cities could change in the future, so they have to be loaded dynamically  */
        loadCityNamesToMemory();

        int selectedOption;
        String selectedCity;
        String[] selectedTimePeriod; // timePeriod[0] = startDate, timePeriod[1] = endDate
        String[] newCredentials;

        currentMenu = GlobalConstants.WELCOME_MENU;
        showActiveMenu();
        selectedOption = readOptionFromUser();

        if (selectedOption == 2) { // create new user
            // Log.info("Staring process to create a new User");

            newCredentials = askForUsernamePassword(0);
            System.out.println(GlobalConstants.CONFIRM_NEW_USER_CREATED);
            User user = new User("Test", newCredentials[0], newCredentials[1]);

        }

        String[] credentials;
        int totalLoginAttempts = 0;
        do {
            credentials = askForUsernamePassword(totalLoginAttempts);
            totalLoginAttempts++;
        } while (!isValidLogin(credentials));

        if (isValidLogin(credentials)) {
            System.out.println("Erfolgreich Eingeloggt!");
            currentMenu = GlobalConstants.SELECT_DATA_OR_ALL_MENU;
        }

        showActiveMenu();
        selectedOption = readOptionFromUser();
        if (selectedOption == 1) {                /* Ony one city is requested. We can ask for min, max and average */

            currentMenu = CITY_NAMES_WITH_INDEX_MENU;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            selectedCity = availableCities[selectedOption];
            System.out.println(String.format("Ausgewählte Stadt : %s", selectedCity));
            selectedTimePeriod = getTimePeriod();

            selectedTimePeriod = /* different Date format: yyyy-MM-dd */
                    Arrays.stream(selectedTimePeriod).map(this::transformDateToDifferentFormat).toArray(String[]::new);


            List<WeatherData> weatherdata = databaseOutputFormatter.selectWeatherByDateAndCity(selectedCity,
                    selectedTimePeriod[0],
                    selectedTimePeriod[1]);

            System.out.println(String.format("%s Printing the first %d entries", ANSI_GREEN, LIMIT_ROWS));
            // print the first N rows
            List<WeatherData> weatherDataTrimmed = weatherdata.stream().limit(LIMIT_ROWS).collect(Collectors.toList());
            System.out.println(ANSI_GREEN + weatherDataTrimmed + ANSI_RESET);


            currentMenu = GlobalConstants.METADATA;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            if (selectedOption == 1) {  // Average
               String avg = databaseOutputFormatter.selectAverageWeatherDataSingleCity(selectedCity, selectedTimePeriod[0], selectedTimePeriod[1] );
               System.out.println(ANSI_YELLOW + avg + ANSI_RESET);
            } else if (selectedOption == 2) { // Minima
                String avg = databaseOutputFormatter.selectMinWeatherDataSingleCity(selectedCity, selectedTimePeriod[0], selectedTimePeriod[1] );
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);

            } else if (selectedOption == 3) {  // Maxima
                String avg = databaseOutputFormatter.selectMaxWeatherDataSingleCity(selectedCity, selectedTimePeriod[0], selectedTimePeriod[1] );
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);
            }

        } else if (selectedOption == 2) { /* All cities are considered. */

            selectedTimePeriod = getTimePeriod();
            String[] timePeriod_DifferentFormat = // different Date format: yyyy-MM-dd
                    Arrays.stream(selectedTimePeriod).map(this::transformDateToDifferentFormat).toArray(String[]::new);

            List<List<WeatherData>> completeWeatherDataList =
                    databaseOutputFormatter.selectWeatherOfAllCitiesInTimeframe(timePeriod_DifferentFormat[0],
                            timePeriod_DifferentFormat[1], availableCities);

            // print the first N rows for each City
            System.out.println(String.format("%s Printing the first %d entries of each city", ANSI_GREEN, LIMIT_ROWS / 2));
            List<List<WeatherData>> weatherDataTrimmed = new ArrayList<>();
            for (List<WeatherData> list : completeWeatherDataList) {
                weatherDataTrimmed.add(list.stream().limit(LIMIT_ROWS / 2).collect(Collectors.toList()));
            }

            weatherDataTrimmed.forEach(WeatherOfCity -> {
                System.out.println(ANSI_GREEN + WeatherOfCity + ANSI_RESET);
            });

        /*
            List<String> max_min;
            max_min = databaseOutputFormatter.selectMaxWeatherDataAllCity(timePeriod_DifferentFormat[0] + " 00:00:00");
            System.out.println(max_min);

             */

        }

    }


    /**
     * For each city, write it's name and coresponding index.
     * We need this to display it in Terminal, so that a sinle city can be selected by user.
     *
     * @param cities
     */
    public String generateCityWithIndex(final String[] cities) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < cities.length; i++) {
            line.append(i).append("  ").append(cities[i]).append("\n");
        }
        line.append("\n Bitte den Index der gewünschten Ortschaft eingeben.");
        return line.toString();
    }

    /**
     * Read user input: Numerical in all cases. The actual meaning of the number can be read from the currentMenu.
     *
     * @return the action the user takes as a numerical value.
     */
    public int readOptionFromUser() {

        int eingabe = -1;
        List<Integer> validMenuValues = new ArrayList<>();

        switch (currentMenu) {
            case GlobalConstants.SELECT_DATA_OR_ALL_MENU:
            case GlobalConstants.TIMESPAN:
            case GlobalConstants.WELCOME_MENU:
                validMenuValues = Arrays.asList(1, 2, 0); // 3 valid actions to choose for each menu
                break;
            case GlobalConstants.METADATA:
                validMenuValues = Arrays.asList(1, 2, 3, 0);
        }
        if (currentMenu.equals(CITY_NAMES_WITH_INDEX_MENU)) {
            // A valid menu value is considerd valid,
            // if it passes the condition 0 <= index < cities.length;
            validMenuValues = IntStream.range(0, availableCities.length)
                    .boxed().collect(Collectors.toList());
        }

        do {
            try {
                eingabe = Integer.parseInt(scanner.next());
                if (!validMenuValues.contains(eingabe)) {
                    showActiveMenu();
                }
            } catch (Exception e) {
                /* Clear the current Buffer */
                scanner.nextLine();
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

        currentMenu = GlobalConstants.TIMESPAN;
        showActiveMenu();
        int selectedOption = readOptionFromUser();
        if (selectedOption == 1) {
            Log.info("You have decided to set a custom timePeriod");
            currentMenu = GlobalConstants.SELECT_TIMESPAN_START;
            showActiveMenu();
            String startDate = tryToParseDate();
            String endDate;
            if (!startDate.equals(GlobalConstants.DEFAULT_DATE[0])) { // no default date
                timeframe[0] = startDate;
                currentMenu = GlobalConstants.SELECT_TIMESPAN_END;
                showActiveMenu();
                endDate = tryToParseDate();
            } else {  // here just set the default date for convenience
                timeframe[0] = startDate;
                endDate = GlobalConstants.DEFAULT_DATE[1];
            }
            timeframe[1] = endDate;

        } else if (selectedOption == 2) { // No constraints on the date.
            timeframe[0] = GlobalConstants.MIN_DATE_VALUE;
            timeframe[1] = GlobalConstants.MAX_DATE_VALUE;
        }
        return timeframe;
    }

    /**
     * Try to parse a single Date.
     * Try again, if there are issues with correct format
     */
    public String tryToParseDate() {

        String input = " ";
        String date = "";

        do {
            if (!input.isEmpty()) {
                try {
                    input = scanner.next();
                    if (input.equals(GlobalConstants.DEFAULT_DATE_KEYWORD)) {
                        System.out.printf("Benutze das Standard Datum %s bis %s", GlobalConstants.DEFAULT_DATE[0],
                                GlobalConstants.DEFAULT_DATE[1]);
                        date = GlobalConstants.DEFAULT_DATE[0];
                    } else {
                        input = replacePointsWithDashes(input);
                        if (isValidDate(input)) {
                            date = input;
                        } else {
                            System.out.println("Datumsformat nicht korrekt. Versuchen Sie es noch einmal");
                            showActiveMenu();
                        }
                    }
                } catch (Exception e) {
                    /* Clear the current Buffer */
                    scanner.nextLine();
                    showActiveMenu();
                }
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
     * Expects a Date as String. I
     * Accepted Formats:
     * "27.11.2020"
     * "27-11-2020"
     * 27.11.
     * 26-11-
     *
     * @return True if the the input String is a valid Date, according to the specificed DATA_FORMAT
     */
    public boolean isValidDate(String date) {
        date = replacePointsWithDashes(date);
        try {
            DateFormat df = new SimpleDateFormat(GlobalConstants.DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            Log.warn(String.format("%s is not a valid date.", date));
        }
        return false;
    }

    public String replacePointsWithDashes(String input) {
        if (input.contains(".")) {  // unify format
            input = input.replaceAll("\\.", "-");
        }
        return input;
    }


    /**
     * TODO: Determine if login is successful.
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
            System.out.print(GlobalConstants.WARN_INVALID_LOGIN);
        }
        String[] credentials = new String[2];
        // removed scanner
        do {
            try {
                System.out.print(GlobalConstants.ASK_USERNAME);
                String username = scanner.next();
                System.out.print(GlobalConstants.ASK_PASSWORD);
                String password = scanner.next();
                if (simpleLoginValidationPassed(username, password)) {
                    credentials[0] = username;
                    credentials[1] = password;
                } else {
                    System.out.println(GlobalConstants.WARN_LOGIN_VALIDATION_NOT_PASSED);
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
        if (username.length() < GlobalConstants.MIN_LENGTH_USERNAME || username.length() > GlobalConstants.MAX_USERNAME_LEN) {

            Log.info(String.format("Username Length should be at least %d and not longer than %d",
                    GlobalConstants.MIN_LENGTH_USERNAME,
                    GlobalConstants.MAX_USERNAME_LEN));
            return false;
        }
        if (password.length() < GlobalConstants.MIN_PASSWORD_LENGTH) {
            Log.info(String.format("The minimum Password length is %d ", GlobalConstants.MIN_PASSWORD_LENGTH));
            return false;
        }
        return true;
    }

    /**
     * function to translate date from dd-mm-yyyy to yyyy-mm-dd
     *
     * @param input Date to be formatted
     * @return Date in new format (essentially flipped )
     */

    public String transformDateToDifferentFormat(String input) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = null;
        try {
            formattedDate = LocalDate.parse(input, formatter).format(formatter2);
        } catch (Exception e) {
            Log.info("failed parsing the Date");
            e.printStackTrace();
        }
        return formattedDate;
    }
}
