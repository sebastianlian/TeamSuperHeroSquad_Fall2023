package Controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

import Model.*;
import View.Console;
import org.yaml.snakeyaml.Yaml;

public class Game {

    static State state;
    static CommandManager commandManager;
    private ArrayList<String> startingPrompts;
    //static State populateRandomItem;

    //Moved code from console to Game class
    public Game() {
        startingPrompts = new ArrayList<>();
        //Add starting prompts here
        startingPrompts.add("-----------------------------------------");
        startingPrompts.add("Welcome to Grizzly Survival!");
        startingPrompts.add("-----------------------------------------");
        startingPrompts.add("You just recently got accepted by Grizzly University " +
                "and you're starting out your first semester as a student. ");
        startingPrompts.add("-----------------------------------------");

    }

    public void getPlayerName() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your character's name: ");
        String playerName = scan.nextLine();
        System.out.println("Hello, " + playerName + "! Let's start your adventure.");
    }

    public void displayStartingPrompts() {
        for (String prompt : startingPrompts) {
            dotdotdot(prompt, 300, 1); // Adjust the duration and number of dots as needed
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
    // Call methods from State class

    public void loadCharacterData() {
        state.loadCharacterData();
    }

    public int selectCharacter() {
        return state.selectCharacter();
    }

    public Map<String, Object> getSelectedCharacter(int characterId) {
        return state.getSelectedCharacter(characterId);
    }

    public static void main(String[] args) throws Exception {

        state = new State(Game::parseItems, Game::parseRooms, Game::parseMonsters);
        commandManager = new CommandManager();

        for (Item item : state.getIndexOfItems().values()) {
            if (item.getId() <= 60) {
                ItemReference itemRef = new ItemReference(item.getId(), item.getName(), item.getId());
                state.populateRandomItem(itemRef);
            }
        }

        //Implement parsePuzzle to create completed Puzzle class (do not pass into State)
//        parsePuzzle();

        //TODO: user setup for the game

        Game game = new Game();
        game.displayStartingPrompts();
        game.getPlayerName();
        game.loadCharacterData();
        int selectedCharacterId = game.selectCharacter();
        Map<String, Object> selectedCharacter = game.getSelectedCharacter(selectedCharacterId);
        if (selectedCharacter != null) {
            System.out.println("You selected: " + selectedCharacter.get("name"));
        } else {
            System.out.println("Invalid character selection.");
        }


//        Scanner scan = new Scanner(System.in);
//        System.out.println("Please enter your character's name: ");
//        String playerName = scan.nextLine();
//        System.out.println("Hello, " + playerName + "! Let's start your adventure.");
        while (state.isRunning()) {
            //TODO: user setup for the game
            Scanner scan = new Scanner(System.in);

            //TODO: initial prompt
            System.out.println("Enter a command: ");

            String console = scan.next().toUpperCase(); //FIXME: remove toUpperCase() after all comparisons ignore caps
            String cmdAttr = scan.nextLine().replaceFirst("^\\s+", ""); //consumes the rest of the line and removes first whitespace

            Room currentRoom = state.getCurrentRoom(); //TODO: why not make currentRoom and outlets protected within state?
            int[] currentRoomOutlets = state.getCurrentOutlets();

            //TODO: failed switch, get it? okay well we'll get rid of this later. Only here for a fallback for non-matching mathod-command pairs
            switch (console) {
                case "N", "NORTH", "UP":
                    commandManager.move(0);
//                    dotdotdot("Moving to a new room", "Arrived within " + state.getRoom(currentRoomOutlets[0]).getName(), 10, 3);
                    break;
                case "W", "WEST", "LEFT":
                    commandManager.move(3);
//                    dotdotdot("Moving to a new room", "Arrived within " + state.getRoom(currentRoomOutlets[0]).getName(), 10, 3);
                    break;
                case "E", "EAST", "RIGHT":
                    commandManager.move(1);
//                    dotdotdot("Moving to a new room", "Arrived within " + state.getRoom(currentRoomOutlets[0]).getName(), 10, 3);
                    break;
                case "S", "SOUTH", "DOWN":
                    commandManager.move(2);
//                    dotdotdot("Moving to a new room", "Arrived within " + state.getRoom(currentRoomOutlets[0]).getName(), 10, 3);
                    break;
                case "PICKUP":
                    commandManager.pickup_item(cmdAttr);
                    break;
                case "DROP":
                    commandManager.drop_item(cmdAttr);
                    break;
                case "USE":
                    commandManager.use_item(cmdAttr);
                    break;
                case "LIST":
                    commandManager.list(cmdAttr);
                    break;
                default:
                    commandManager.validateCommand(console, cmdAttr);
            }
        }
    }

    public static HashMap<Integer, Item> parseItems() throws Exception {
        // Index of items.
        HashMap<Integer, Item> itemIndex = new HashMap<>();

        // Parses YAML file.
        Yaml yaml = new Yaml();
        String source = Files.readString(Paths.get("items.yaml"));
        Map<String, Object> object = yaml.load(source);

        // Creates object mappings from YAML data.
        ArrayList<Object> items = (ArrayList<Object>) object.get("items");
        for (Object item : items) {
            Map<Object, Object> mapping = (Map<Object, Object>) item;

            // Extract all fields for Item
            int itemID = (int) mapping.get("id");
            String itemName = (String) mapping.get("name");
            boolean itemType = ((int) mapping.get("type") == 1); //returns true or false based on type number
            String itemEffect = (String) mapping.get("effect");
            String itemDescription = (String) mapping.get("description");
            //int quantity = (int) mapping.get("quantity");

            Item itemInstance = new Item(
                    itemID,
                    itemName,
                    itemType,
                    itemEffect,
                    itemDescription
            );

            itemIndex.put(itemID, itemInstance);
        }

        return itemIndex;
    }


    public static HashMap<Room, int[]> parseRooms() throws Exception {
        // List of rooms.
        HashMap<Room, int[]> rooms = new HashMap<>();

        // Parses YAML file.
        Yaml yaml = new Yaml();
        String source = Files.readString(Paths.get("rooms.yaml"));
        Map<String, Object> object = yaml.load(source);

        // Creates room instances and outlet mappings from YAML data.
        ArrayList<Object> objects = (ArrayList<Object>) object.get("rooms");
        for (Object room : objects) {
            Map<Object, Object> mapping = (Map<Object, Object>) room;

            Room roomInstance = new Room(
                    (int) mapping.get("id"),
                    (String) mapping.get("name"),
                    (String) mapping.get("description"),
                    (ArrayList<Integer>) mapping.get("items"),
                    (int) mapping.getOrDefault("attempt",0),
                    (String)mapping.get("topic"),
                    (boolean) mapping.getOrDefault("hasPuzzle",false)
            );
            Map<Object, Integer> outletMapping = (Map<Object, Integer>) mapping.get("outlets");
            int[] outlets = new int[]{-1, -1, -1, -1};
            outlets[0] = outletMapping.getOrDefault("north", -1);
            outlets[1] = outletMapping.getOrDefault("east", -1);
            outlets[2] = outletMapping.getOrDefault("south", -1);
            outlets[3] = outletMapping.getOrDefault("west", -1);

            rooms.put(roomInstance, outlets);
        }

        return rooms;
    }

    public static HashMap<Integer, Actor> parseMonsters() throws Exception {
        // List of monsters.
        HashMap<Integer, Actor> monsters = new HashMap<>();

        // Parses YAML file.
        Yaml yaml = new Yaml();
        String source = Files.readString(Paths.get("monsters.yaml"));
        Map<String, Object> object = yaml.load(source);

        // Creates monster (actor) instances from YAML data.
        ArrayList<Object> objects = (ArrayList<Object>) object.get("monsters");
        for (Object actor : objects) {
            Map<Object, Object> mapping = (Map<Object, Object>) actor;

            int id = (int) mapping.get("id");
            Map<Object, Object> stats = (Map<Object, Object>) mapping.get("stats");
            Actor monsterInstance = new Actor(
//                    (int)mapping.get("id"),
                    (String) mapping.get("name"),
                    (String) mapping.get("description"),
                    Double.valueOf((int)stats.getOrDefault("hp", 0)),
                    Double.valueOf((int)stats.getOrDefault("def", 0)),
                    Double.valueOf((int)stats.getOrDefault("atk", 0)),
                    (int) mapping.get("location"), //setting monster location using the room id NOT THE startingLocation value
                    (String) mapping.get("type")

            );
//            System.out.println("Debug: Name=" + mapping.get("name") + ", HP=" + monsterInstance.getHitPoints() + ", DEF=" + monsterInstance.getAttack() + ", ATK=" + mapping.get("atk"));

//            Map<Object, Integer> outletMapping = (Map<Object, Integer>)mapping.get("outlets");
//            int[] outlets = new int[] { -1, -1, -1, -1 };
//            outlets[0] = outletMapping.getOrDefault("north", -1);
//            outlets[1] = outletMapping.getOrDefault("east", -1);
//            outlets[2] = outletMapping.getOrDefault("south", -1);
//            outlets[3] = outletMapping.getOrDefault("west", -1);

            monsters.put(id, monsterInstance);
        }

        return monsters;
    }

    //TODO: implement parsePuzzle()
//    public static HashMap<Integer, Actor> parsePuzzle() throws Exception {
//        // List of monsters.
//        HashMap<Integer, Actor> puzzles = new HashMap<>();
//
//        // Parses YAML file.
//        Yaml yaml = new Yaml();
//        String source = Files.readString(Paths.get("puzzles.yaml"));
//        Map<String, Object> object = yaml.load(source);
//
//        // Creates monster (actor) instances from YAML data.
//        LinkedHashMap<Object, ArrayList<Object>> objects = (LinkedHashMap<Object, ArrayList<Object>>) object.get("Puzzles");
//        System.out.println(objects.size());
//
//        //First layer objects in puzzle are topics, second layer objects are question, answer
//        for (Map.Entry<Object, ArrayList<Object>> entry : objects.entrySet()) {
//            System.out.println(entry.getValue());
//            System.out.println();
////            for (Object puzzle :
////                    entry.getValue()) {
////                System.out.println("Prompt of all puzzles: ");
////                System.out.println(puzzle);
////            }
//        }
//        return puzzles;
//    }
}