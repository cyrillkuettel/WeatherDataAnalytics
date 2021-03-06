package ch.hslu.swde.wda;

public final class GlobalConstants {


    public static final String MIN_DATE_VALUE = "01-02-2021"; // inclusive
    public static final String MAX_DATE_VALUE = "30-11-2021";

    public static final String[] DEFAULT_DATE = {"28-11-2021", "30-11-2021"};

    public static final int MIN_LENGTH_NAME = 2;
    public static final int MIN_PASSWORD_LENGTH = 5;
    public static final String WEATHERDATA_PROVIDER = "http://swde.el.eee.intern:8080/weatherdata-provider/";
    public static final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata" +
            "/cities";
    public static final String STANDARD_SURNAME = "username";
    public static final String STANDARD_FORENAME = "username";
    public static final String STANDARD_PASSWORD = "password";

    /**
     * Hardcoded cities. This is only for local testing.
     */
    public static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St.Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St.Gallen"};


    public static final String SELECT_TIMESPAN_END = "Enddatum: ";
    public static final String DEFAULT_DATE_KEYWORD = "skip";
    public static final String SELECT_TIMESPAN_START =
            "Startdatum angeben. Erwartetes Format ist \"dd.MM.yyyy\" \n\"" + DEFAULT_DATE_KEYWORD + "\" für das " +
                    "Standart " +
                    "Datum";

    public static final String ASK_FORANME = "Bitte Vorname eingeben: ";
    public static final String ASK_SURNAME = "Bitte Nachname eingeben: ";
    public static final String ASK_PASSWORD = "Bitte Passwort eingeben: ";
    public static final String CONFIRM_NEW_USER_CREATED = "Benutzererstellung erfolgreich.";
    public static final String REFUTE_NEW_USER_CREATED = "Benutzererstellung nicht erfolgreich.";
    public static final String WELCOME_MENU = "Start [1]      Neuer Benutzer erstellen [2]      vorhandene Benutzer bearbeiten [3]";
    public static final String TIMESPAN = "Zeitspanne festlegen [1]        gesamter Zeitraum (2020 - 2021) [2]       " +
            "   ";
    public static final String SELECT_DATA_OR_ALL_MENU =
            "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über " +
                    "alle " + "Ortschaften laden [2] " + "     " +
                    "Beenden " + "[0]";
    public static final String WARN_INVALID_LOGIN = "Ist nicht eine korrekte Benutzername / Password Kombination. " +
            "Versuchen Sie es noch einmal \n";
    public static final String WARN_LOGIN_VALIDATION_NOT_PASSED = "Das ist ein ungülties Passwort / Benutzername. " +
            "Passwort muss mindestens 8 Zeichen lang sein, Benutzername mindestens 3 Zeichen";
    public static final String METADATA = "Durchschnittswerte dieser Ausgabe berechnen [1]     Maxima dieser Ausgabe berechnen [2]     Mimima dieser Ausgabe berechnen [3]     Beenden [0]";


    public static final String METADATA_ALL_CITY =
            "Maxima aller Ortschaften in diesem Zeitraum berechnen [1]     Maxima aller Ortschaften in diesem Zeitraum berechnen [2]     Beenden [0]";
    public static final String USER_EDIT = "Index des Benutzers angeben.";
    public static final String SELECT_ACTION_FOR_USER = "Aktion: Update User [1]     Delete User[2]    ";
    public static final String DOWNLOAD_MENU = "Wetterdaten Herunterladen?  CSV Herunterladen [1]      Überspringen " +
            "[2]    ";


    // for colored output in unix-like terminals
    public static final String ANSI_YELLOW = "\033[33m";
    public static final String ANSI_GREEN = "\033[32m";
    public static final String ANSI_CYAN = "\033[36m";
    public static final String ANSI_RESET = "\033[0m";

    public static final int LIMIT_ROWS = 10;
    public static final int MAXIMUM_NUMBER_OF_USERS = 1000;


    public static final String SELECT_MAX_OR_MIN_OR_DOWNLOAD = "Maximum [1]        Minimum [2]        [3] Download " +
            "File and exit";

    public static final String SELECT_TEMP_PRESSURE_OR_HUMIDITY = "Temperatur [1]        Druck [2]        " +
            "Feuchtigkeit [3]";

}
