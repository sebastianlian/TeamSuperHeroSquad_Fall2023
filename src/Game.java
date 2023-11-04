import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import Model.Room;
import Model.State;
import Model.Item;

public class Game {

    static State state;

    public static void main(String[] args) throws Exception {
        System.out.println("IA2, ver. 0.0.1.");
        System.out.println("Developed by Camden Johnson <git@cosinami.com>.");
        System.out.println("All Rights Reserved.\n");

        state = new State(Game::parseItems, Game::parseRooms);

        System.out.println("Hello. Welcome to the game.");
        System.out.println("This is Grizzly survival.");
        System.out.println("It is a Zombie game.");
        System.out.println("This is for Software Development 1");
        System.out.println("We hope you have a lot of fun playing!");

    }

    public static HashMap<Integer, Item> parseItems() throws Exception {
        // Index of items.
        HashMap<Integer, Item> itemIndex = new HashMap<>();

        // Parses YAML file.
        Yaml yaml = new Yaml();
        String source = Files.readString(Paths.get("items.yaml"));
        Map<String, Object> object = yaml.load(source);

        // Creates object mappings from YAML data.
        ArrayList<Object> items = (ArrayList<Object>)object.get("items");
        for (Object item : items) {
            Map<Object, Object> mapping = (Map<Object, Object>)item;

         // Extract all fields for Item
            int itemID = (int)mapping.get("id");
            String itemName = (String)mapping.get("name");
            String itemType = (String)mapping.get("type");
            String itemEffect = (String)mapping.get("effect");
            int quantity = (int)mapping.get("quantity");

            Item itemInstance = new Item(
            itemID,
            itemName,
            itemType,
            itemEffect,
            quantity
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
        ArrayList<Object> objects = (ArrayList<Object>)object.get("rooms");
        for (Object room : objects) {
            Map<Object, Object> mapping = (Map<Object, Object>)room;

            Room roomInstance = new Room(
                    (int)mapping.get("number"),
                    (String)mapping.get("name"),
                    (String)mapping.get("description"),
                    (ArrayList<Integer>)mapping.get("items")
            );

            Map<Object, Integer> outletMapping = (Map<Object, Integer>)mapping.get("outlets");
            int[] outlets = new int[] { -1, -1, -1, -1 };
            outlets[0] = outletMapping.getOrDefault("north", -1);
            outlets[1] = outletMapping.getOrDefault("east", -1);
            outlets[2] = outletMapping.getOrDefault("south", -1);
            outlets[3] = outletMapping.getOrDefault("west", -1);

            rooms.put(roomInstance, outlets);
        }

        return rooms;
    }

}