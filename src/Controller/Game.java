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

    // Call methods from State class


    public int selectCharacter() {
        return state.selectCharacter();
    }


    public static void main(String[] args) throws Exception {
        //First initalize main instance of View (Scanner) and Controller objects
        Scanner scan = new Scanner(System.in);
        commandManager = new CommandManager();
        //Load from file or init setup~
        if (loadFromFile(scan)) {
            state = commandManager.load();
        } else {
            state = new State(Game::parseItems, Game::parseRooms, Game::parseMonsters);
            Puzzle puzzle = new Puzzle(Puzzle.topic.All);

            ConsoleTUI.displayStartingPrompts();
            ConsoleTUI.getPlayerName();

        }

        for (Item item : state.getIndexOfItems().values()) {
            if (item.getId() <= 60) {
                ItemReference itemRef = new ItemReference(item.getId(), item.getName(), item.getId());
                state.populateRandomItem(itemRef);
            }
        }

        System.out.println(state.getCurrentRoom().getRoomDescription());


        while (state.isRunning()) {

            System.out.println("Enter a command: ");
            System.out.println("Type 'HELP' for list of commands");


            String console = scan.next().toUpperCase();
            String cmdAttr = scan.nextLine().replaceFirst("^\\s+", ""); //consumes the rest of the line and removes first whitespace

            Room currentRoom = state.getCurrentRoom();
            int[] currentRoomOutlets = state.getCurrentOutlets();

            switch (console) {
                case "N", "NORTH", "UP":
                    commandManager.move(0);
                    break;
                case "W", "WEST", "LEFT":
                    commandManager.move(3);
                    break;
                case "E", "EAST", "RIGHT":
                    commandManager.move(1);
                    break;
                case "S", "SOUTH", "DOWN":
                    commandManager.move(2);
                    break;
                case "HELP":
                    commandManager.access_help();
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
            int number = (int) mapping.get("number");
            Map<String, Integer> stats = (Map<String, Integer>) mapping.getOrDefault("stats", null);
            Map<String, Integer> statsMap = (Map<String, Integer>) mapping.get("stats");

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
                        itemEffect,
                        itemDescription,
                        number,
                        itemStats
                );
            } else {
                itemInstance = new Item(
                        itemID,
                        itemName,
                        itemType,
                        itemEffect,
                        itemDescription,
                        number
                );
            }
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
                    (boolean) mapping.getOrDefault("hasPuzzle",false),
                    (boolean) mapping.getOrDefault("locked",false)
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

                    (String) mapping.get("name"),
                    (String) mapping.get("description"),
                    Double.valueOf((int)stats.getOrDefault("hp", 0)),
                    Double.valueOf((int)stats.getOrDefault("def", 0)),
                    Double.valueOf((int)stats.getOrDefault("atk", 0)),
                    (int) mapping.get("location"), //setting monster location using the room id NOT THE startingLocation value
                    (String) mapping.get("type")

            );

            monsters.put(id, monsterInstance);
        }

        return monsters;
    }

}