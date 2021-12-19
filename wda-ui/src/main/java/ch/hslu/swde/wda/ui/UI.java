package ch.hslu.swde.wda.ui;


import ch.hslu.swde.wda.NetworkUtils.Utils;
import ch.hslu.swde.wda.business.BusinessHandler;
import ch.hslu.swde.wda.domain.User;
import ch.hslu.swde.wda.domain.WeatherData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.AccessControlException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
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

    // deprecated. Instead of DatabaseOutputFormatter, we use RMI now.
    // DatabaseOutputFormatter databaseOutputFormatter = new DatabaseOutputFormatter();

    /**
     * Probably the most important field of the application.
     * All RMI flow through this.
     */
    BusinessHandler stub;


    public UI() {
        scanner = new Scanner(System.in);
        if (!Utils.pingURL(WEATHERDATA_PROVIDER, 10000)) {
            Log.fatal(ANSI_YELLOW + "Could not ping swde.el.ee.intern:80. Are you connected to https://vpn.hslu.ch?" +
                              ANSI_RESET);
        }
        stub = createStub();

    }

    /**
     * RMI Initialization.
     * @return The stub Object.
     */
    public BusinessHandler createStub() {

        // Server: 10.177.6.157;
        // cyrill's laptop: 10.155.231.41
        final String rmiServerIP = "10.177.6.157"; // change this
        final int rmiPort = 1099;

        configureSecurityManager();

        final String url = "rmi://" + rmiServerIP + ":" + rmiPort + "/" + BusinessHandler.RO_NAME;
        Log.info(url);

        BusinessHandler stub;
        try {
            stub = (BusinessHandler) Naming.lookup(url);
            Log.info("created Stub");
            return stub;
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            Log.info("Naming.lookup has thrown an Exception. Stub could not be created. ");
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Helper funciton for RMI.
     * Security Manger is currently allowing all permissions.
     */
    public void configureSecurityManager() {
        final String projectDir = System.getProperty("user.dir"); // for example: /home/cyrill/Desktop/g07-wda
        Log.info(String.format("System.getProperty(\"user.dir\") == %s", projectDir));
        final String clientPolicyRelativeDir = "/wda-ui/client.policy";
        String policy = String.format("file:%s%s", projectDir, clientPolicyRelativeDir);
        Log.info(policy);

        // adjust path if necesary
        policy = removeReduntantPathDirectory(policy);


        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy", policy);
            System.setSecurityManager(new SecurityManager());
            Log.info(String.format("Installed Security manager at %s", policy));
        } else {
            Log.info("There is already an installed security manager. java.security.policy might not have been set");
        }

        try {
            Log.info(System.getProperty("java.security.policy"));
        } catch (AccessControlException e) {  // "trick" to check if the security.policy has been set
            Log.fatal("Could not read the java.security.policy file. Probably because the specified path  does not " +
                              "exist.");
        }
    }

    /**
     * This is a hacky way to correct incorrect path. The file path for client.policy)is based on the "user.dir.
     * This property is not static, but actually based on the path from where the main method is called.
     * This sometimes results in unexpected behaviour.
     * Basically, in some instances, System.getProperty("user.dir") returns a different path than what one might
     * expect. For example, if the  UI class is created in the context of unit test, the user.dir is based on the test
     * directory, therefore deviating from the actual runtime path. This method addresses this problem.
     * @param path The "wrong" poath to be corrected.
     * @return returns the Path to the security.policy file.
     */
    public static String removeReduntantPathDirectory(String path) {
        final String word = "wda-ui/";
        int count = 0;
        String copyOfPath = path;
        while (copyOfPath.contains(word)) {
            copyOfPath = copyOfPath.replaceFirst(word, "");
            count++;
        }
        if (count == 2) {
            path = path.replaceFirst(word, "");
            return path;
        }
        return path;
    }


    /**
     * Start a new User interaction. ( CLI Loop )
     * @throws RemoteException If RMI communication with stub throws this Exception.
     */
    public void startFromBeginning() throws RemoteException {



        /* The cities could change in the future, so they will to be fetched at starttime dynamically  */
        loadCityNamesToMemory();

        int selectedOption;

        String[] selectedTimePeriod; // timePeriod[0] = startDate, timePeriod[1] = endDate
        User user;
        int totalLoginAttempts = 0;
        do {
            user = askForCredentials(totalLoginAttempts);
            totalLoginAttempts++;
        } while (!isValidLogin(user));

        System.out.println("Erfolgreich Eingeloggt!");

        boolean userManagemntLoop;
        do {
            userManagemntLoop = showUserManagementDialog();
        } while (userManagemntLoop);
        currentMenu = SELECT_DATA_OR_ALL_MENU;
        showActiveMenu();
        selectedOption = readOptionFromUser();
        if (selectedOption == 1) {                /* Ony one city is requested. We can ask for min, max and average */
            currentMenu = CITY_NAMES_WITH_INDEX_MENU;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            String selectedCity = availableCities[selectedOption];
            System.out.printf("Ausgewählte Stadt : %s%n", selectedCity);
            selectedTimePeriod = getTimePeriod();

            selectedTimePeriod = /* different Date format: yyyy-MM-dd */
                    Arrays.stream(selectedTimePeriod).map(this::transformDateToDifferentFormat).toArray(String[]::new);

            final List<WeatherData> weatherdata = stub.selectWeatherByDateAndCity(selectedCity,
                                                                            selectedTimePeriod[0],
                                                                            selectedTimePeriod[1]);

            System.out.printf("%s Printing the first %d entries%n", ANSI_GREEN, LIMIT_ROWS);
            List<WeatherData> weatherDataTrimmed = weatherdata.stream().limit(LIMIT_ROWS).collect(Collectors.toList());
            System.out.println(ANSI_GREEN + weatherDataTrimmed + ANSI_RESET);

            plotTemperature(weatherdata);

            currentMenu = DOWNLOAD_MENU;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            if (selectedOption == 1) {
                downloadFileFromServer();
            }
            loop_ToggleMaximumAndMinimumAndAverage(selectedCity, selectedTimePeriod);
        } else if (selectedOption == 2) { /* All cities are considered. */
            selectedTimePeriod = getTimePeriod();
            String[] timePeriod_DifferentFormat = // different Date format: yyyy-MM-dd
                    Arrays.stream(selectedTimePeriod).map(this::transformDateToDifferentFormat).toArray(String[]::new);

            final List<List<WeatherData>> completeWeatherDataList =
                    selectWeatherOfAllCitiesInTimeframe(timePeriod_DifferentFormat[0],
                                                        timePeriod_DifferentFormat[1],
                                                        availableCities);

            // print the first N rows for each City
            System.out.printf("%s Printing the first %d entries of each city%n", ANSI_GREEN, LIMIT_ROWS / 2);
            List<List<WeatherData>> weatherDataTrimmed = new ArrayList<>();
            for (List<WeatherData> list : completeWeatherDataList) {
                weatherDataTrimmed.add(list.stream().limit(LIMIT_ROWS / 2).collect(Collectors.toList()));
            }

            weatherDataTrimmed.forEach(WeatherOfCity -> {
                System.out.println(ANSI_GREEN + WeatherOfCity + ANSI_RESET);
            });

            currentMenu = DOWNLOAD_MENU;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            if (selectedOption == 1) {
                downloadFileFromServer();
            }

            loop_ToggleMaximumAndMinimum(completeWeatherDataList);

        }
    }

    private boolean downloadFileFromServer() throws RemoteException {
        byte [] mydata = stub.downloadWeatherDataAsCSV();
        System.out.println("downloading...");

        final String downloadDirectoy = System.getProperty("user.dir");
        final String fileName = "WeatherData.csv";
        final String realpath = downloadDirectoy + "/" + fileName;
        File clientpathfile = new File(realpath);
        try (FileOutputStream out = new FileOutputStream(clientpathfile)) {
            out.write(mydata);
            out.flush();
            out.close();
            System.out.printf("Downloaded File to %s", realpath);
        } catch (Exception ex) {
            Log.warn("There was an Exception writing file.");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Shows the user managment menu, as long as the User does not select Option 1.
     * When the user has selected option 1, this means he has finisehd the user administration and wants to use the
     * application. In this case, we return False.
     *
     * @return True if the user has not selected the option [1] start.
     * @throws RemoteException if there was an exception in the RMI connection.
     */
    public boolean showUserManagementDialog() throws RemoteException {
        currentMenu = WELCOME_MENU;
        showActiveMenu();
        int selectedOption = readOptionFromUser();
        if (selectedOption == 1) {
            return false;
        }
        if (selectedOption == 2) { // create new user

            User newUser = askForCredentials(0);

            if (stub.insertUser(newUser)) {
                System.out.println(CONFIRM_NEW_USER_CREATED);
            } else {
                System.out.println(REFUTE_NEW_USER_CREATED);
            }

        } else if (selectedOption == 3) { // edit users.
            List<User> allUsers = stub.getUserNamesAsList();

            Map<Integer, User> usersWithIndex = IntStream.range(0, allUsers.size())
                    .boxed()
                    .collect(Collectors.toMap(Function.identity(), allUsers::get));

            usersWithIndex.forEach((key, usr) -> {  // display all users on screen
                System.out.printf("%s %s %s %s%n", key, usr.getUserid(), usr.getFirstname(),
                                  usr.getLastname());
            });

            currentMenu = USER_EDIT;
            showActiveMenu();
            User selectedUser;
            int index;
            do {
                index = readOptionFromUser();
                selectedUser = usersWithIndex.get(index);
            } while (!usersWithIndex.containsKey(index));

            System.out.printf("Benutzer %s (%s) ausgewählt.%n", selectedUser.getFirstname(),
                              selectedUser.getUserid());
            currentMenu = SELECT_ACTION_FOR_USER;
            showActiveMenu();
            selectedOption = readOptionFromUser();
            if (selectedOption == 1) { // update
                System.out.println("Benutzerinformationen anpassen. . . ");
                User updatedUser;

                updatedUser = askForCredentials(0);

                // now update with the new user information
                stub.updateUser(updatedUser);

            } else if (selectedOption == 2) { // delete
                System.out.println("Benutzer löschen. . . ");
                boolean success = stub.deleteUser(selectedUser);
                if (success) {
                    System.out.printf("Benutzer %s gelöscht!%n", selectedUser.getFirstname());
                } else {
                    System.out.printf("Fehler beim löschen von Benutzer %s%n",
                                      selectedUser.getFirstname());
                }
            }
        }
        return true;
    }


    /**
     * Function calls selectWeatherByDateAndCity on the stub. This just executes the function on all Cities, which
     * are a parameter. Then a nested List is returned, for each city the weather data.
     * @param start Inclusive start Date.
     * @param end Inclusive start Date.
     * @param availableCities The Array of cities for which to get the Data.
     * @throws RemoteException Exception in RMI.
     * @return Nested List, each entry is corresponds to one city.
     */
    public List<List<WeatherData>> selectWeatherOfAllCitiesInTimeframe(final String start, final String end,
                                                                       final String[] availableCities) throws RemoteException {
        List<List<WeatherData>> dataOfAllCities = new ArrayList<>();
        for (String city : availableCities) {
            List<WeatherData> list = stub.selectWeatherByDateAndCity(city, start, end);
            dataOfAllCities.add(list);
        }
        return dataOfAllCities;
    }

    public void plotTemperature(List<WeatherData> weatherData) {
        System.out.println(ANSI_CYAN);
        System.out.println("Plot Temperatur. X-Achse: Zeit / Y-Achse: temp (C)");
        Comparator<WeatherData> weatherDataComparator = Comparator.comparing(WeatherData::getDataTimestamp);
        weatherData.sort(weatherDataComparator);
        final double[] temperatures = weatherData.stream().map(WeatherData::getTemp).mapToDouble(v -> v).toArray();
        final List<Timestamp> timestamps =
                weatherData.stream().map(WeatherData::getDataTimestamp).collect(Collectors.toList());

        if (temperatures.length > 0) {
            System.out.println();
            System.out.println(ch.hslu.swde.wda.ui.ASCIIGraph.fromSeries(temperatures)
                                       .withTickFormat(new DecimalFormat("##0.00"))
                                       .plot());
            System.out.println();
            System.out.println(ANSI_RESET);
        }
    }

    /**
     * Loop and prompt User for input on selection option Min or Max.
     * @param completeWeatherDataList This is the result of the selectWeatherOfAllCitiesInTimeframe method. They are
     *                                called inside startFromBeginning method.
     * @throws RemoteException Exception in RMI.
     */
    public void loop_ToggleMaximumAndMinimum(List<List<WeatherData>> completeWeatherDataList) throws RemoteException {
        currentMenu = METADATA_ALL_CITY;
        showActiveMenu();
        int selectedOption = readOptionFromUser();

        while (selectedOption != 0) {

            if (selectedOption == 1) {  // Minima
                // get the oldest possible timeStamp to get a Valid Timestamp range
                Timestamp minimumTimeStamp_forThisWeatherData = completeWeatherDataList.stream()
                        .flatMap(List::stream)
                        .map(WeatherData::getDataTimestamp)
                        .max(Timestamp::compareTo).get(); // TODO: isPresent Check

                String avg = stub.selectMinWeatherDataAllCity(minimumTimeStamp_forThisWeatherData);
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);

            } else if (selectedOption == 2) { // Maxima

                // to get a Valid Timestamp, get the 'oldest' TimeStamp that is available
                Timestamp minimumTimeStamp_forThisWeatherData = completeWeatherDataList.stream()
                        .flatMap(List::stream)
                        .map(WeatherData::getDataTimestamp)
                        .max(Timestamp::compareTo).get();

                String avg = stub.selectMaxWeatherDataAllCity(minimumTimeStamp_forThisWeatherData);
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);
            }
            currentMenu = METADATA_ALL_CITY;
            showActiveMenu();
            selectedOption = readOptionFromUser();
        }
    }

    /**
     * Loop and prompt User for input on selection option:  average, min or max.
     * Note that this method is almost idential to {@link UI#loop_ToggleMaximumAndMinimum(List)}
     * However, this method has extended functinality, is supports the average option. That has been implemented like
     * that just because the 'average' option is only provided for single places.
     * @throws RemoteException Exception in RMI.
     * @param city City for which to select this data.
     * @param timePeriod Limit the selection to this timeperiod.
     * @throws RemoteException Exception in RMI.
     */
    public void loop_ToggleMaximumAndMinimumAndAverage(String city, String[] timePeriod) throws RemoteException {
        currentMenu = METADATA;
        showActiveMenu();
        int selectedOption = readOptionFromUser();

        while (selectedOption != 0) {
            if (selectedOption == 1) {  // Average
                String avg =
                        stub.selectAverageWeatherDataSingleCity(city, timePeriod[0], timePeriod[1]);
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);
            } else if (selectedOption == 2) { // Minima
                String avg = stub.selectMinWeatherDataSingleCity(city, timePeriod[0], timePeriod[1]);
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);

            } else if (selectedOption == 3) {  // Maxima
                String avg = stub.selectMaxWeatherDataSingleCity(city, timePeriod[0], timePeriod[1]);
                System.out.println(ANSI_YELLOW + avg + ANSI_RESET);
            }
            currentMenu = METADATA;
            showActiveMenu();
            selectedOption = readOptionFromUser();
        }
    }


    /**
     * For each city, write it's name and coresponding index.
     * We need this to display it in Terminal, so that a sinle city can be selected by user.
     * @param cities Array of Cities for which to generate the index.
     * @return String, seperated by newline character.
     * *
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
     * @return the action the user takes as a numerical value.
     */
    public int readOptionFromUser() {

        int eingabe = -1;
        List<Integer> validMenuValues = new ArrayList<>();

        switch (currentMenu) {
            case SELECT_DATA_OR_ALL_MENU:
            case TIMESPAN:
            case METADATA_ALL_CITY:
            case SELECT_ACTION_FOR_USER:
            case DOWNLOAD_MENU:
                validMenuValues = Arrays.asList(1, 2, 0); // 3 valid actions to choose for each menu
                break;
            case METADATA:
            case WELCOME_MENU:
                validMenuValues = Arrays.asList(1, 2, 3, 0);
                break;
            case USER_EDIT: // we check for valid index when calling the method
                validMenuValues = IntStream.range(0, MAXIMUM_NUMBER_OF_USERS).boxed().collect(Collectors.toList());
                break;
        }
        if (currentMenu.equals(CITY_NAMES_WITH_INDEX_MENU)) {
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
     * Prompt user input to specify Timespan. If the user does not want to restric search,
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
            } else {  // here just set the default date for convenience
                timeframe[0] = startDate;
                endDate = DEFAULT_DATE[1];
            }
            timeframe[1] = endDate;

        } else if (selectedOption == 2) { // No constraints on the date.
            timeframe[0] = MIN_DATE_VALUE;
            timeframe[1] = MAX_DATE_VALUE;
        }
        return timeframe;
    }

    /**
     * Try to parse a single Date.
     * As simple as it sounds, this is actually a very meticulous affair.
     * The format may be wrong in a number of different ways - this has to be checked hence the size of the method.
     * Because the process is kind of inconvenient, you can select the default date with keyword.
     * {@link ch.hslu.swde.wda.GlobalConstants#DEFAULT_DATE_KEYWORD}
     * @return The Date.
     */
    public String tryToParseDate() {

        String input = " ";
        String date = "";

        do {
            if (!input.isEmpty()) {
                try {
                    input = scanner.next();
                    if (input.equals(DEFAULT_DATE_KEYWORD)) {
                        System.out.printf("Benutze das Standard Datum %s bis %s", DEFAULT_DATE[0],
                                          DEFAULT_DATE[1]);
                        date = DEFAULT_DATE[0];
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
     * @param date The Date to be checked for validity.
     * @return True if the the input String is a valid Date, according to the specificed DATA_FORMAT.
     */
    public boolean isValidDate(String date) {
        final String DATE_FORMAT = "dd-MM-yyyy";
        date = replacePointsWithDashes(date);
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
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
     * Compare all current user from database with the input User.
     * Comparision is being done with the equals method of User.
     * @return true if the login succeeds, false otherwise.
     * @throws RemoteException Exception in RMI.
     */
    private boolean isValidLogin(User validateThisUser) throws RemoteException {
        List<User> currentUserList = stub.getUserNamesAsList();
        //  note: from a security standpoint, this is is inferior.
        // validation should probably be done completly on the server, without reading all users.
        return currentUserList.stream().anyMatch(user -> user.equals(validateThisUser));
    }

    /**
     * Ask the user to type in forename, surname and password. It will Loop indefinitely, until the condition in
     * {@link #simpleLoginValidationPassed(String[]) condition passes}
     * @param attemptCount How many times the user has already attempted to log in.
     * @return String Array containing the username [index: 0] and password. [index: 1].
     */
    public User askForCredentials(final int attemptCount) {
        if (attemptCount > 0) {
            System.out.print(WARN_INVALID_LOGIN);
        }
        String[] credentials = new String[3];
        do {
            try {
                System.out.print(ASK_FORANME);
                String forename = scanner.next();
                System.out.print(ASK_SURNAME);
                String surname = scanner.next();
                System.out.print(ASK_PASSWORD);
                String password = scanner.next();
                if (simpleLoginValidationPassed(new String[]{forename, surname, password})) {
                    credentials = new String[]{forename, surname, password};
                } else {
                    System.out.println(WARN_LOGIN_VALIDATION_NOT_PASSED);
                }
            } catch (Exception e) {
                Log.error("Error while reading Username or Password in method askForUsernamePassword", e);
                System.err.println("\n Fehler beim Lesen der Login Daten");
            }
        } while (Arrays.stream(credentials).anyMatch(Objects::isNull));
        return new User(credentials[0], credentials[1], credentials[2]);

    }

    /**
     * This Function already checks if the username and password pass certain basic validations.
     * This is not a security measure, it just prevents sending malformed data to our server: For example: the username
     * cannot be an empty String.
     * @return true if the credidentals are valid, false if there is problem.
     * @param creds the Data to be validated.
     */
    public boolean simpleLoginValidationPassed(String[] creds) {

        if (creds[0].length() < MIN_LENGTH_NAME || creds[1].length() < MIN_LENGTH_NAME) {

            Log.info(String.format("Username Length should be at least %d characters", MIN_LENGTH_NAME));
            return false;
        }
        if (creds[2].length() < MIN_PASSWORD_LENGTH) {
            Log.info(String.format("The minimum Password length is %d ", MIN_PASSWORD_LENGTH));
            return false;
        }
        return true;
    }

    /**
     * function to translate date from dd-mm-yyyy to yyyy-mm-dd.
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

    public String[] convertListToArray(List<String> input) {
        return input.toArray(new String[0]);
    }


    /**
     * We want to have access to the cities as soon as the application starts.
     * This is because the menu provides selection options of cities.
     * @throws RemoteException Exception in RMI.
     */
    public void loadCityNamesToMemory() throws RemoteException {
        availableCities = convertListToArray(stub.getCityNamesAsList());
        CITY_NAMES_WITH_INDEX_MENU = generateCityWithIndex(availableCities);
    }

    private void loadUsersIntoMemory() throws RemoteException {
        User finn = new User("Finn", "Morgenthaler", "test1");
        User cyrill = new User("Cyrill", "Küttel", "test2");
        User lenny = new User("Lenny", "Buddliger", "test3");
        User nilu = new User("Nilukzil", "Fernando", "test4");

        stub.insertUser(cyrill);
        stub.insertUser(nilu);
        stub.insertUser(lenny);
        stub.insertUser(finn);

    }

}
