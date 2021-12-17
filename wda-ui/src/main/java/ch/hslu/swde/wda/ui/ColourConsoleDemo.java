package ch.hslu.swde.wda.ui;

public class ColourConsoleDemo {

    /**
     * Example of ANSI sequences to print colored output on terminal.
     */

    public static void main(String[] args) {
        System.out.println("\033[0m BLACK");
        System.out.println("\033[31m RED");
        System.out.println("\033[32m GREEN");
        System.out.println("\033[33m YELLOW");
        System.out.println("\033[34m BLUE");
        System.out.println("\033[35m MAGENTA");
        System.out.println("\033[36m CYAN");
        System.out.println("\033[37m WHITE");
    }
}