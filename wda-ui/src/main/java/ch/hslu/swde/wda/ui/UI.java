package ch.hslu.swde.wda.ui;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * Main Loop for Console Application
 */
public final class UI {
    private static final Logger Log = LogManager.getLogger(UI.class);

    /* Menu Constants */

    private static final String WELCOME_MENU = "Hallo! Bitte loggen sie sich ein.";
    private static final String MENU_START = "Wetterdaten von einer Ortschaft laden [1]     " + "Wetterdaten über alle" +
            "     " +
            "Ortschaften hinweg laden [2] " +
            "     " +
            "Beenden " + "[0]";

    /* Start-Menu */
    private String currentMenu;

    private final String[] cities = {"Lausanne", "Geneva", "Nyon", "Biel", "Bern", "Thun", "Adelboden",
            "Interlaken",
            "Grindelwald", "Lauterbrunnen", "Meiringen", "Brig", "Saas-Fee", "Zermatt", "Basel", "Solothurn", "Olten",
            "Aarau", "Baden", "Lucerne", "Buchrain", "Zug", "Rotkreuz", "Engelberg", "Schwyz", "Altdorf", "Erstfeld",
            "Andermatt", "Realp", "Bellinzona", "Locarno", "Airolo", "Chur", "Arosa", "Davos", "St. Moritz", "Zurich",
            "Winterthur", "Frauenfeld", "St. Gallen"};


    public UI() {
        execute();
    }


    public void execute() {


        showLogin();
        currentMenu = MENU_START;

        showActiveMenu();
        int selectedOption = eingabeEinlesen();
        if (selectedOption == 1) { /* eine Ortschaft */
            currentMenu = generateCityWithIndex();
            showActiveMenu();
            selectedOption = eingabeEinlesen();

        } else if (selectedOption == 2) { /* alle Ortschaften */

        }

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

        List<Integer> values = new ArrayList<>();

        /* Use a switch because there will be more Options in the future */
        switch (currentMenu) {
            case MENU_START:
                values = Arrays.asList(1, 2, 0);
                break;
            default:
                // not neccessary
        }
        /* current Menu determines the valid values*/

        do {
            try {
                eingabe = sc.nextInt();
                if (!values.contains(eingabe)) {
                    showActiveMenu();
                }
            } catch (Exception e) {
                /* Clear the current Buffer */
                sc.nextLine();
                showActiveMenu();
            }

        } while (!values.contains(eingabe));

        return eingabe;
    }

    private List<String> showLogin() {
        System.out.println(WELCOME_MENU);
        List<String> list = new ArrayList<>();

        Scanner sc = new Scanner(System.in);

        try {
            System.out.println();
            System.out.print("Username: ");
            String name = sc.nextLine();
            System.out.print("Password: ");
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
     */
    public String generateCityWithIndex() {
        String line = "";
        for (int i = 0; i < cities.length; i++) {
            line += i + "  " + cities[i] + "\n";
        }
        line += "\n Bitte den Index der gewünschten Ortschaft eingeben.";
        return line;
    }



}

