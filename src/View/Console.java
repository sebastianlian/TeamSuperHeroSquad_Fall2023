package View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Console {

    private static ArrayList<String> startingPrompts;

    public Console() {
        startingPrompts = new ArrayList<>();
        //Add starting prompts here
        startingPrompts.add("Welcome to the Grizzly University Adventure!");
        startingPrompts.add("Navigate the treacherous floors and defeat the Dean to save the university.");
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
        //Additional prints for different game phases or events
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

    public static void main(String[] args) throws InterruptedException {
        Console gameConsole = new Console();
        gameConsole.allPrints();
    }
}