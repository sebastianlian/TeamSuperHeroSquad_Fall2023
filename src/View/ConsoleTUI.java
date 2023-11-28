package View;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConsoleTUI implements Serializable {

    //FIXME: static for little benefit
//    private static ArrayList<String> startingPrompts = new ArrayList<>();
    //TODO: create internal static class to hold strings
    static String breakLine = "-----------------------------------------\n";
    static String welcome1 = "Welcome to Grizzly Survival!\n";
    static String welcome2 = "You just recently got accepted by Grizzly University \nand you're starting out your first semester as a student. \n";

    public static void getPlayerName() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your character's name: ");
        String playerName = scan.nextLine();
        System.out.println("Hello, " + playerName + "! Let's start your adventure.");
    }

    public static void displayStartingPrompts() throws InterruptedException {
        printLettersBy(breakLine, 10);
        printLettersBy(welcome1, 45);
        printLettersBy(breakLine, 10);
        printLettersBy(welcome2, 45);

//        for (String prompt : startingPrompts) {
//            printLettersBy(prompt); // Adjust the duration and number of dots as needed
//        }
    }

    public static void dotdotdot(String message, int delay, int repetitions) {
        System.out.print(message);
        for (int i = 0; i < repetitions; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
                System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void dotdotdot(String message) {
        int delay = 300;
        int repetitions = 3;
        dotdotdot(message, delay, repetitions);
    }


//    public static void printLettersBy(String text) throws InterruptedException {
//        for (char c :
//                text.toCharArray()) {
//            System.out.println(c);
//            TimeUnit.MILLISECONDS.sleep(30);
//        }
//    }

    public static void printLettersBy(String text, long delay) {
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            System.out.print(text.substring(i, i + 1));
        }
    }

    public static void initialTestPrint() throws InterruptedException {
        printLettersBy("Running initial tests", "Tests completed", 3, 3);
    }

//    public void initialGamePrint() throws InterruptedException {
//        printLettersBy("Initializing game", "Game ready", 2, 4);
//        for (String prompt : startingPrompts) {
//            System.out.println(prompt);
//        }
//    }

    //TODO: assess if this needs to be here
    public void getPlayerName(Scanner scan) {
        System.out.println("Please enter your character's name: ");
        String playerName = scan.nextLine();
        System.out.println("Hello, " + playerName + "! Let's start your adventure.");
    }

    public void exitGamePrint() throws InterruptedException {
        printLettersBy("Exiting game", "Goodbye!", 2, 3);
    }

    public void allPrints() throws InterruptedException {
        initialTestPrint();
//        initialGamePrint();
        //Add more prints as needed
        // Additional prints for different game phases or events
        exitGamePrint();
    }

    public void roomFormatPrint(String roomDescription) {
        System.out.println("You are in " + roomDescription + ".");
    }

    public static void printLettersBy(String str1, String str2, long time, int dots) throws InterruptedException {
        //TODO: switch logic from dotdotdot when ready

        System.out.print(str1);
        for (int i = 0; i < dots; i++) {
            TimeUnit.MILLISECONDS.sleep(100 * time);
            System.out.print(".");
        }
        System.out.println(str2);
        TimeUnit.MILLISECONDS.sleep(100 * time);
    }

    // DEAD CODE
    public static void main(String[] args) throws InterruptedException {
        ConsoleTUI gameConsoleTUI = new ConsoleTUI();
        gameConsoleTUI.allPrints();

    }
}