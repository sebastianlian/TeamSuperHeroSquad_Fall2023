package View;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//TODO: Sebastian write console class
public class Console {

    private static ArrayList<String> startingPrompts;


    public Console() {
        startingPrompts = new ArrayList<>();
        //Add starting prompts here

        startingPrompts.add("Welcome to Grizzly Survival!");
        startingPrompts.add("Press enter to start the game.");

    }
    public void  getPlayerName() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your character's name: ");
        String playerName = scan.nextLine();
        System.out.println("Hello, " + playerName + "! Let's start your adventure.");
    }


    public void displayStartingPrompts() {
        for (String prompt : startingPrompts) {
            dotdotdot(prompt, 350, 1); // Adjust the duration and number of dots as needed
        }
    }
    private void dotdotdot(String message, long delay, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            System.out.print(message);
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
                System.out.print(".");
                TimeUnit.MILLISECONDS.sleep(delay);
                System.out.print(".");
                TimeUnit.MILLISECONDS.sleep(delay);
                System.out.println(".");
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialTestPrint() throws InterruptedException {
        dotdotdot("Running initial tests", "Tests completed", 3, 3);
    }

    public void initialGamePrint() throws InterruptedException {
        dotdotdot("Initializing game", "Game ready", 2, 4);
        for (String prompt : startingPrompts) {
            System.out.println(prompt);
        }
    }

    public void exitGamePrint() throws InterruptedException {
        dotdotdot("Exiting game", "Goodbye!", 2, 3);
    }

    public void allPrints() throws InterruptedException {
        initialTestPrint();
        initialGamePrint();
        //Add more prints as needed
       // Additional prints for different game phases or events
        exitGamePrint();
    }

    public void roomFormatPrint(String roomDescription) {
        System.out.println("You are in " + roomDescription + ".");
    }

    public static void dotdotdot(String str1, String str2, long time, int dots) throws InterruptedException {
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
        Console gameConsole = new Console();
        gameConsole.allPrints();

    }
}