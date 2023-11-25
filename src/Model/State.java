package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    protected Room currentRoom;
    private List<Map<String, Object>> characters;

    private Puzzle



    // Player Variables
    private Actor player; //We don't want our player to be an Actor TODO: remove and replace with a proper stats
    private ArrayList<ItemReference> inventory;
    private int position = 0; // FIXME: There must be a 0th room.
    private int characterID; //FIXME: what is character id for?
    private double hitPoints, defense, attack;

    //Aux player variables
    private ItemReference equippedItem = null;




    public State(Callable<HashMap<Integer, Item>> populateItemIndex, Callable<HashMap<Room, int[]>> populateRooms, Callable<HashMap<Integer, Actor>> populateMonsters) throws Exception {
        running = true;
        gameMode = MODE.PLAY;

        inventory = new ArrayList<>();

        indexedItems = populateItemIndex.call();
        System.out.println("[DEBUG] Indexed Items: " + indexedItems.size());

        indexedRooms = populateRooms.call();
        System.out.println("[DEBUG] Indexed Rooms: " + indexedRooms.size());

        indexedMonsters = populateMonsters.call();
        System.out.println("[DEBUG] Indexed Monsters: " + indexedMonsters.size());

        for (Actor monster :
                indexedMonsters.values()) {
        }

        setInitalRoom();

        hitPoints = 100;
        defense = 100;
        attack = 100;

        Iterator<Actor> monsterIterator = indexedMonsters.values().iterator();
        for (Room room : indexedRooms.keySet()) {
            //TODO: replace foreach with iterators and create references for each object related to rooms
            room.getItems().forEach((itemInput) -> createItemRefInstance(itemInput, room));
//            room.getMonsters().forEach((monsterInput) -> createMonsterRefInstance(monsterInput, room, indexedMonsters.values().stream().filter(e -> e.getId() == monsterInput).reduce((mon, mon2) -> {
//                throw new IllegalStateException("Multiple elements: " + mon + ", " + mon2);
//            }).get()));
        }
    }

    public void setInitalRoom() {
        this.currentRoom = getRoom(position);
    }

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

    public boolean isRunning() {
        return running;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public int[] getCurrentOutlets() {
        return indexedRooms.get(currentRoom);
    }

//FIXME:FORLATER

//    public void beginBattle(MonsterReference monsterReference) {
//        setGameMode(MODE.BATTLE);
//        currentMonster = monsterReference;
//    }

    private void createItemRefInstance(int itemId, Room selectRoom) {
        ItemReference itemRef = new ItemReference(itemId, indexedItems.get(itemId).getName(), selectRoom.getRoomID());
        selectRoom.referredItems.put(itemId, itemRef);
    }
    //
//    private void createMonsterRefInstance(int monsterId, Room selectRoom, Actor monster) {
//        MonsterReference monRef = new MonsterReference(monsterId, indexedMonsters.get(monsterId).getName(), selectRoom.getNumber(), monster);
//
//        monsters.add(monRef);
//        selectRoom.referredMonsters.put(monsterId, monRef);
//    }
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

    public void moveIntoInventory(ItemReference itemRef) {
        //Remove item from room and add item to player inventory

        if (itemRef == null) {
            System.out.println(itemRef + " failed to be moved into inventory");
        } else {
            inventory.add(itemRef);
            currentRoom.referredItems.remove(itemRef.getIndex());

        }
    }
    public void replenishHP(double amount) {
        // Ensuring that HP does not exceed the maximum value
        hitPoints = Math.min(hitPoints + amount, 100);
        System.out.println("HP replenished. Current HP: " + hitPoints);
    }
    public void replenishMaxHP() {
        // Setting HP to the maximum value
        replenishHP(100); //FIXME
        System.out.println("Max HP replenished. Current HP: " + hitPoints);
    }
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
    public void loadCharacterData() {
        Yaml yaml = new Yaml();
        Path path = Paths.get("character.yaml");
        try (InputStream inputStream = Files.newInputStream(path)) {
            Map<String, List<Map<String, Object>>> data = yaml.load(inputStream);
            characters = data.get("characters");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public int selectCharacter() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your character:");
        System.out.println("For IT majors, type '1'. For Business Majors, type '2'. For Nursing Majors, type '3'.");

        for (Map<String, Object> character : characters) {
            System.out.println(character.get("id") + ". " + character.get("name"));
        }

        int selectedId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        return selectedId;
    }
    public Map<String, Object> getSelectedCharacter(int characterId) {
        for (Map<String, Object> character : characters) {
            if ((int) character.get("id") == characterId) {
                return character;
            }
        }
        return null; // Character not found
    }

    public void equipItem(ItemReference itemRef) {
        if (itemRef == null) {
            System.out.println(itemRef + " failed to be moved into inventory");
        } else {
            equippedItem = itemRef;
            inventory.remove(itemRef);
        }
    }

    public void removeEquippedItem(ItemReference itemRef) {
        if (itemRef == null) {
            System.out.println(itemRef + " failed to be moved into inventory");
        } else {
            inventory.add(itemRef);
            equippedItem = null;
        }
    }


    public void replenishHP(double amount) {
        // Ensuring that HP does not exceed the maximum value
        hitPoints = Math.min(hitPoints + amount, 100);
        System.out.println("HP replenished. Current HP: " + hitPoints);
    }
    public void replenishMaxHP() {
        // Setting HP to the maximum value
        replenishHP(100); //FIXME
        System.out.println("Max HP replenished. Current HP: " + hitPoints);
    }
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
    public void loadCharacterData() {
        Yaml yaml = new Yaml();
        Path path = Paths.get("character.yaml");
        try (InputStream inputStream = Files.newInputStream(path)) {
            Map<String, List<Map<String, Object>>> data = yaml.load(inputStream);
            characters = data.get("characters");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public int selectCharacter() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your character:");
        System.out.println("For IT majors, type '1'. For Business Majors, type '2'. For Nursing Majors, type '3'.");

        for (Map<String, Object> character : characters) {
            System.out.println(character.get("id") + ". " + character.get("name"));
        }

        int selectedId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        return selectedId;
    }
    public Map<String, Object> getSelectedCharacter(int characterId) {
        for (Map<String, Object> character : characters) {
            if ((int) character.get("id") == characterId) {
                return character;
            }
        }
        return null; // Character not found
    }

    //TODO: refactor these
    //Getters and setters
    public double getHitPoints() {
        return hitPoints;
    }

    public double getDefense() {
        return defense;
    }

    public double getAttack() {
        return attack;
    }

    public ArrayList<ItemReference> getInventory() {
        return inventory;
    }

    //TODO: all my homies hate getters and setters, so we'll GET rid of these below


    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
    public void setCurrentRoom(int roomNumber) {
        this.currentRoom = getRoom(roomNumber);
    }

    public void displayInventory() {
        List itemInInventory = getInventory().stream().map(ItemReference::getName).collect(Collectors.toList());
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory contains:");
            System.out.println(itemInInventory);
        }
    }

    public void accessMap(){
        int[] test = indexedRooms.get(currentRoom);

        System.out.println(test);

    }

    public HashMap<Integer, Item> getIndexOfItems() {
        return indexedItems;
    }

    public Item getItem(int i) {
        return indexedItems.get(i);
    }

    public void consumeStats(Item.Stats incomingStats) {
        hitPoints += incomingStats.hp;
        attack += incomingStats.atk;
        defense += incomingStats.def;
    }

}