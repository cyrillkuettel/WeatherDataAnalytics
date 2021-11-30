package ch.hslu.swde.wda;

public final class Constants {

    // minimum and maximum valid value, inclusive
    public static final String MIN_DATE_VALUE = "01-01-2020";
    public static final String MAX_DATE_VALUE = "31-12-2020";
    public static final int MAX_USERNAME_LEN = 40;
    public static final int MIN_LENGTH_USERNAME = 3;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final String CITY_URL = "http://swde.el.eee.intern:8080/weatherdata-provider/rest/weatherdata" +
            "/cities";
    /**
     * Hardcoded cities. This is only for local testing.
     */
    public static final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St. Gallen"};


    public static final String SELECT_TIMESPAN_END = "Enddatum: ";
    public static final String DEFAULT_DATE_KEYWORD = "skip";
    public static final String SELECT_TIMESPAN_START =
                    "Startdatum angeben. Erwartetes Format ist \"dd.MM.yyyy\" \n\"" + DEFAULT_DATE_KEYWORD + "\" für das " +
                            "Standart " +
                            "Datum";
    public static final String ASK_PASSWORD = "Bitte Passwort eingeben: ";
    public static final String ASK_USERNAME = "Bitte Benutzername eingeben: ";
    public static final String CONFIRM_NEW_USER_CREATED = "Erstellung erfolgreich.";
    public static final String WELCOME_MENU = " Einloggen [1]      Neuer Benutzer erstellen [2] ";
    public static final String TIMESPAN = "Zeitspanne festlegen [1]        gesamter Zeitraum [2]          " +
                                    "Beenden [0]";
    public static final String SELECT_DATA_OR_ALL_MENU =
                                            "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über " +
                                                    "alle " + "Ortschaften laden [2] " + "     " +
                                                    "Beenden " + "[0]";
    public static final String WARN_INVALID_LOGIN = "Ist nicht eine korrekte Benutzername / Password Kombination. " +
                                                            "Versuchen Sie es noch einmal";
    public static final String WARN_LOGIN_VALIDATION_NOT_PASSED = "Das ist ein ungülties Passwort / Benutzername. " +
                                                                    "Passwort muss mindestens 8 Zeichen lang sein, Benutzername mindestens 3 Zeichen";
    public final static String DATE_FORMAT = "dd-MM-yyyy";
    public static final String[] DEFAULT_DATE = {"29-11-2021", "30-11-2021"};
}
