package Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

enum MODE {
    //FIXME: Temp enum to discern mode until permanent way decided
    PLAY,
    BATTLE

}

public class State implements Serializable {
    // Statics
    protected HashMap<Integer, Item> indexedItems;


    // Controller.Game State
    private HashMap<Room, int[]> indexedRooms;

    private HashMap<Integer, Actor> indexedMonsters;
    private boolean running;
    private MODE gameMode;
    //    protected MonsterReference currentMonster = null;
    protected Room currentRoom, previousRoom;

    //Puzzle init here


    // Player Variables
    private Actor player; //We don't want our player to be an Actor TODO: remove and replace with a proper stats
//    private ArrayList<ItemReference> inventory;
    private HashSet<ItemReference> inventory;

    private int position = 0; // FIXME: There must be a 0th room.
    private int characterID; //FIXME: what is character id for?
    private double hitPoints, maxHitPoints, defense, attack;

    //Aux player variables
    private ItemReference equippedItem = null;
    private Stats playerStats;


    public State(Callable<HashMap<Integer, Item>> populateItemIndex, Callable<HashMap<Room, int[]>> populateRooms, Callable<HashMap<Integer, Actor>> populateMonsters) throws Exception {
        running = true;
        gameMode = MODE.PLAY;

//        inventory = new ArrayList<>();
        inventory = new HashSet<>();

        indexedItems = populateItemIndex.call();
        System.out.println("[DEBUG] Indexed Items: " + indexedItems.size());

        inventory.add(new ItemReference(indexedItems.get(10)));

        indexedRooms = populateRooms.call();
        System.out.println("[DEBUG] Indexed Rooms: " + indexedRooms.size());

        indexedMonsters = populateMonsters.call();
        System.out.println("[DEBUG] Indexed Monsters: " + indexedMonsters.size());


        for (Actor monster :
                indexedMonsters.values()) {
        }

        setInitalRoom();

        initialPlayerSetup();

        Iterator<Actor> monsterIterator = indexedMonsters.values().iterator();
        for (Room room : indexedRooms.keySet()) {
            //TODO: replace foreach with iterators and create references for each object related to rooms
            room.getItems().forEach((itemInput) -> createItemRefInstance(itemInput, room));
//            room.getMonsters().forEach((monsterInput) -> createMonsterRefInstance(monsterInput, room, indexedMonsters.values().stream().filter(e -> e.getId() == monsterInput).reduce((mon, mon2) -> {
//                throw new IllegalStateException("Multiple elements: " + mon + ", " + mon2);
//            }).get()));
        }
    }

    private void initialPlayerSetup() throws IOException {
        loadCharacterData();

        hitPoints = maxHitPoints = playerStats.getHp();
        attack = playerStats.getAtk();
        defense = playerStats.getDef();

    }

    //Internal (private) method containing inital room logic inside state (State -> State)
    public void setInitalRoom() {
        this.currentRoom = getRoom(position);
        this.currentRoom.setVisited();
    }

    //Getter for Room instances (? <- State)
    public Room getRoom(int roomNum) {
        Room foundRoom = indexedRooms
                .keySet()
                .stream()
                .filter(e -> e.getRoomID() == roomNum)
                .reduce((room, room2) -> {
                    throw new IllegalStateException("Multiple elements: " + room + ", " + room2);
                })
                .get();

        return foundRoom;
    }

    //Getter for running check (? <- State)
    public boolean isRunning() {
        return running;
    }

    //Getter for current Room instances (? <- State)
    public Room getCurrentRoom() {
        return currentRoom;
    }

    //Getter for current Room instanced outlets (? <- State)
    public int[] getCurrentOutlets() {
        return indexedRooms.get(currentRoom);
    }

    //Internal (private) method to create ItemReferences and match to Room (State -> State)
    private void createItemRefInstance(int itemId, Room selectRoom) {
        ItemReference itemRef = new ItemReference(itemId, indexedItems.get(itemId).getName(), selectRoom.getRoomID());
        selectRoom.referredItems.put(itemId, itemRef);
    }

    //Internal (private) method to move ItemReferences between Room and inventory (State -> State)
    public void moveFromInventory(ItemReference itemRef) {
        //Remove item from player inventory and add item to room

        if (itemRef == null) {
            //TODO: move fail message?
            System.out.println(itemRef + " failed to be moved from inventory");
        } else {
            currentRoom.referredItems.put(itemRef.getIndex(), itemRef);
            inventory.remove(itemRef);
        }
    }

    //Internal (private) method to move ItemReferences between inventory and Room (State -> State)
    public void moveIntoInventory(ItemReference itemRef) {
        //Remove item from room and add item to player inventory

        if (itemRef == null) {
            System.out.println(itemRef + " failed to be moved into inventory");
        } else {
            inventory.add(itemRef);
            currentRoom.referredItems.remove(itemRef.getIndex());
        }
    }

    //?
    public void replenishHP(double amount) {
        // Ensuring that HP does not exceed the maximum value
        hitPoints = Math.min(hitPoints + amount, 100);
        System.out.println("HP replenished. Current HP: " + hitPoints);
    }

    //?
    public void replenishMaxHP() {
        // Setting HP to the maximum value
        replenishHP(100); //FIXME
        System.out.println("Max HP replenished. Current HP: " + hitPoints);
    }

    //?
    public void takePlayerDamage(double damage) {
        // Ensure that the player's HP doesn't go below 0
        hitPoints = Math.max(hitPoints - damage, 0);
        System.out.println("Player took " + damage + " damage. Current HP: " + hitPoints);

        // Check if the player has run out of HP
        if (hitPoints == 0) {
            running = false; // Game over
            System.out.println("Game Over. Player has run out of HP.");
        }
    }

    public void loadCharacterData() throws IOException {

        // Parses YAML file.
        Yaml yaml = new Yaml();
        String source = Files.readString(Paths.get("character.yaml"));

        // Index of items.
        HashMap<Integer, Item> itemIndex = new HashMap<>();

        Map<String, Object> object = yaml.load(source);

        int roleNum = selectCharacter() - 1 ;

        // Creates object mappings from YAML data.
        ArrayList<Object> characters = (ArrayList<Object>) object.get("characters");
        Object role = characters.get(roleNum);
        Map<Object, Object> mapping = (Map<Object, Object>) role;

        // Extract all fields for Item
        String roleName = (String) mapping.get("name");
        String roleDescription = (String) mapping.get("description");
        Map<String, Integer> stats = (Map<String, Integer>) mapping.getOrDefault("stats", null);

        Stats roleStats = new Stats(
                Double.valueOf(stats.getOrDefault("hp", 0)),
                Double.valueOf(stats.getOrDefault("def", 0)),
                Double.valueOf(stats.getOrDefault("atk", 0))
        );
        ArrayList<Integer> startingRoleItems = (ArrayList<Integer>) mapping.get("bonus items");
        ArrayList<ItemReference> startingRoleItemRef = new ArrayList<>();
        for (Integer item :
                startingRoleItems) {
            startingRoleItemRef.add(new ItemReference(indexedItems.get(item)));
        }
        //FIXME role information has not place to go

        playerStats = roleStats;
        inventory.addAll(startingRoleItemRef);
    }

    //?
    public int selectCharacter() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your character:");
        System.out.println("For IT majors, type '1'. For Business Majors, type '2'. For Nursing Majors, type '3'.");

//        for (Map<String, Object> character : characters) {
//            System.out.println(character.get("id") + ". " + character.get("name"));
//        }

        int selectedId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        return selectedId;
    }

    //Internal (private) method to equip ItemReference (State -> State)
    public void equipItem(ItemReference itemRef) {
        if (itemRef == null) {
            System.out.println(itemRef + " failed to be moved into inventory");
        } else {
            equippedItem = itemRef;
            inventory.remove(itemRef);
        }
    }

    //Internal (private) method to unequip ItemReference (State -> State)
    public void removeEquippedItem(ItemReference itemRef) {
        if (itemRef == null) {
            System.out.println(itemRef + " failed to be moved into inventory");
        } else {
            inventory.add(itemRef);
            equippedItem = null;
        }
    }

    //TODO: refactor these
    //Player Getters and setters
    public double getHitPoints() {
        return hitPoints;
    }

    public double getDefense() {
        return defense;
    }

    public double getAttack() {
        return attack;
    }

    public HashSet<ItemReference> getInventory() {
        return inventory;
    }

    //TODO: all my homies hate getters and setters, so we'll GET rid of these below


    public boolean setCurrentRoom(Room currentRoom) {
        this.previousRoom = this.currentRoom;
        this.currentRoom = currentRoom;

//        if (!currentRoom.isFirstVisit()) {
//            currentRoom.setFirstVisit(true);
//            return;
//        } else if (currentRoom.isFirstVisit()) {
//            currentRoom.setFirstVisit(false);
//        }
        this.previousRoom.setVisited();
        return true;
    }

    public boolean setCurrentRoom(int roomNumber) {
        this.previousRoom = this.currentRoom;
        this.currentRoom = getRoom(roomNumber);
//        if (!currentRoom.isFirstVisit()) {
//            currentRoom.setFirstVisit(true);
//            return;
//        } else if (currentRoom.isFirstVisit()) {
//            currentRoom.setFirstVisit(false);
//        }
        this.previousRoom.setVisited();
        return true;
    }

    //(Controller <- State)
    public void displayInventory() {
        List itemInInventory = getInventory().stream().map(ItemReference::getName).collect(Collectors.toList());
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory contains:");
            System.out.println(itemInInventory);
        }
    }

    public void accessMap() {
        int[] test = indexedRooms.get(currentRoom);

        System.out.println(test);

    }

    public HashMap<Integer, Item> getIndexOfItems() {
        return indexedItems;
    }

    public Item getItem(int i) {
        return indexedItems.get(i);
    }

    public void consumeStats(Item.ItemStats incomingStats) {
        hitPoints += incomingStats.hp;
        attack += incomingStats.atk;
        defense += incomingStats.def;
    }

    /*
    FIXME: expected types of methods inside State include:
        Internal access methods (State -> State) [allows the state to work on itself easier]
        Referencable getters that are finalized on init and not manipulated
        MINIMAL External methods for necessary access
     */

}