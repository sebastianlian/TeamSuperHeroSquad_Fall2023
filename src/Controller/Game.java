package Controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

import Model.*;
import View.ConsoleTUI;
import Model.Actor;
import org.yaml.snakeyaml.Yaml;

import Model.Room;
import Model.State;
import Model.Item;

public class Game {

    static State state;
    static CommandManager commandManager;
    private ArrayList<String> startingPrompts;

    //Moved code from console to Game class
//    public Game () {
//        startingPrompts = new ArrayList<>();
//        //Add starting prompts here
//        startingPrompts.add("-----------------------------------------");
//        startingPrompts.add("Welcome to Grizzly Survival!");
//        startingPrompts.add("-----------------------------------------");
//        startingPrompts.add("You just recently got accepted by Grizzly University " +
//                "and you're starting out your first semester as a student. ");
//        startingPrompts.add("-----------------------------------------");
//
//    }
    public void  getPlayerName() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter your character's name: ");
        String playerName = scan.nextLine();
        System.out.println("Hello, " + playerName + "! Let's start your adventure.");
    }

    public static boolean loadFromFile(Scanner scan) throws Exception {
        //Ask if player wants to load a new file
        System.out.println("Would you like to load from your previous save?");
        System.out.println("1: Yes, 2: No");

        return (scan.nextLine().equals("1"));
    }
//    public void displayStartingPrompts() {
//        for (String prompt : startingPrompts) {
//            dotdotdot(prompt, 300, 1); // Adjust the duration and number of dots as needed
//        }
//    }


    // Call methods from State class

    public void loadCharacterData() {
//        state.loadCharacterData();
    }

    public int selectCharacter() {
        return state.selectCharacter();
    }

//    public Map<String, Object> getSelectedCharacter(int characterId) {
//        return state.getSelectedCharacter(characterId);
//    }

    public static void main(String[] args) throws Exception {
        //First initalize main instance of View (Scanner) and Controller objects
        Scanner scan = new Scanner(System.in);
        commandManager = new CommandManager();

        //Load from file or init setup~
        //TODO: user setup for the game
        if (loadFromFile(scan)) {
            state = commandManager.load();
        } else {
            state = new State(Game::parseItems, Game::parseRooms, Game::parseMonsters);
            Puzzle puzzle = new Puzzle(Puzzle.topic.All);

            ConsoleTUI.displayStartingPrompts();
            ConsoleTUI.getPlayerName();
//            ConsoleTUI.getPlayerName(scan);
            state.loadCharacterData(); //TODO: change all references to the word character to role
            //Dead code: only need to pull 1 role
//            int selectedCharacterId = selectCharacter();
//            Map<String, Object> selectedCharacter = getSelectedCharacter(selectedCharacterId);

            //TODO: place this elsewhere
//            if (selectedCharacter != null) {
//                System.out.println("You selected: " + selectedCharacter.get("name"));
//            } else {
//                System.out.println("Invalid character selection.");
//            }
        }

        while (state.isRunning()) {
            //TODO: initial prompt
            System.out.println("Enter a command: ");

            String console = scan.next().toUpperCase(); //FIXME: remove toUpperCase() after all comparisons ignore caps
            String cmdAttr = scan.nextLine().replaceFirst("^\\s+", ""); //consumes the rest of the line and removes first whitespace

            Room currentRoom = state.getCurrentRoom(); //TODO: why not make currentRoom and outlets protected within state?
            int[] currentRoomOutlets = state.getCurrentOutlets();

            //TODO: failed switch, get it? okay well we'll get rid of this later. Only here for a fallback for non-matching mathod-command pairs
            //TODO: get rid of this switch
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
//            String itemEffect = (String) mapping.get("effect");
            String itemDescription = (String) mapping.get("description");
            Map<String, Integer> stats = (Map<String, Integer>) mapping.getOrDefault("stats", null);

            Item itemInstance;
            if (stats != null) {
                Item.ItemStats itemStats = new Item.ItemStats(
                        Double.valueOf(stats.getOrDefault("hp", 0)),
                        Double.valueOf(stats.getOrDefault("def", 0)),
                        Double.valueOf(stats.getOrDefault("atk", 0))
                );

                itemInstance = new Item(
                        itemID,
                        itemName,
                        itemType,
                        itemDescription,
                        itemStats
                );
            } else {
                itemInstance = new Item(
                        itemID,
                        itemName,
                        itemType,
                        itemDescription
                );
            }

//            int quantity = (int) mapping.get("quantity");
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
                    (ArrayList<Integer>) mapping.get("items")
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

            Actor monsterInstance = new Actor(
//                    (int)mapping.get("id"),
                    (String) mapping.get("name"),
                    (String) mapping.get("description"),
                    Double.valueOf((int) mapping.getOrDefault("hp", 0)),
                    Double.valueOf((int) mapping.getOrDefault("def", 0)),
                    Double.valueOf((int) mapping.getOrDefault("atk", 0)),
                    (int) mapping.get("id") //setting monster location using the room id NOT THE startingLocation value
            );

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