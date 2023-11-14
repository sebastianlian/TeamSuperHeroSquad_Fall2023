package Controller;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Model.Actor;
import org.yaml.snakeyaml.Yaml;

import Model.Room;
import Model.State;
import Model.Item;

public class Game {

    static State state;

    public static void main(String[] args) throws Exception {

        state = new State(Game::parseItems, Game::parseRooms, Game::parseMonsters);

        //Implement parsePuzzle to create completed Puzzle class (do not pass into State)
//        parsePuzzle();

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
//            int quantity = (int) mapping.get("quantity");

            Item itemInstance = new Item(
                    itemID,
                    itemName,
                    itemType,
                    itemEffect
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
                    (int) mapping.get("number"),
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
                    Double.valueOf((int)mapping.getOrDefault("hp", 0)),
                    Double.valueOf((int)mapping.getOrDefault("def", 0)),
                    Double.valueOf((int)mapping.getOrDefault("atk", 0)),
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
    public static HashMap<Integer, Actor> parsePuzzle() throws Exception {
        // List of monsters.
        HashMap<Integer, Actor> puzzles = new HashMap<>();

        // Parses YAML file.
        Yaml yaml = new Yaml();
        String source = Files.readString(Paths.get("puzzles.yaml"));
        Map<String, Object> object = yaml.load(source);

        // Creates monster (actor) instances from YAML data.
        LinkedHashMap<Object, ArrayList<Object>> objects = (LinkedHashMap<Object, ArrayList<Object>>) object.get("Puzzles");
        System.out.println(objects.size());

        //First layer objects in puzzle are topics, second layer objects are question, answer
        for (Map.Entry<Object, ArrayList<Object>> entry : objects.entrySet()) {
            for (Object puzzle :
                    entry.getValue()) {
                System.out.println("Prompt of all puzzles: ");
                System.out.println(puzzle);
            }
        }
        return puzzles;
    }
}