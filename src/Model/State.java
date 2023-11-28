package Model;

import Controller.Game;
import org.yaml.snakeyaml.Yaml;

import java.io.Serializable;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

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

    private Item equipped;
    private Puzzle puzzle = new Puzzle();


    // Player Variables
    private Actor player; //We don't want our player to be an Actor TODO: remove and replace with a proper stats
    private ArrayList<ItemReference> inventory;
    private int position = 0; // FIXME: There must be a 0th room.
    private int characterID; //FIXME: what is character id for?
    private double maxHitPoints , hitPoints, defense, attack;

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

        setInitialRoom();

        hitPoints = maxHitPoints = 100;
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

    public double getMaxHitPoints() {
        return maxHitPoints;
    }


    public void healPlayer(double amount) {
        this.hitPoints += amount;
        // Ensure that HP does not exceed maximum HP, if there's a limit
    }


    public Actor getMonsterInCurrentRoom() {
        int currentRoomId = currentRoom.getRoomID();

        return indexedMonsters.values().stream()
                .filter(monster -> monster.getCurrentPosition() == currentRoomId)
                .findFirst()
                .orElse(null);
    }
    public void removeMonster() {
        Actor monster = getMonsterInCurrentRoom();
        if (monster != null) {
            indexedMonsters.values().remove(monster);
        }
    }


    public void setInitialRoom() {
        this.currentRoom = getRoom(position);
    }

    public HashMap<Room, int[]> getIndexedRooms() {
        return indexedRooms;
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
        Item item = indexedItems.get(itemId);
        ItemReference itemRef = new ItemReference(itemId, indexedItems.get(itemId).getName(), selectRoom.getRoomID(),item);
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

    //FIXME: MUST TAKE INTO ACCOUNT QUANTITY OF AN ITEM AVAILABLE.
    public void populateRandomItem(ItemReference itemRef) {
        Random random = new Random();
        List<Item> allItems = new ArrayList<>(indexedItems.values());
        List<Room> rooms = new ArrayList<>(indexedRooms.keySet());

        // Select a random item and room
        Item randomItem = allItems.get(random.nextInt(allItems.size()));
        Room randomRoom = rooms.get(random.nextInt(rooms.size()));


        while (randomItem.getId() == 100 && !Arrays.asList(4, 9, 14, 19).contains(randomRoom.getRoomID())) {
            randomItem = allItems.get(random.nextInt(allItems.size()));
            randomRoom = rooms.get(random.nextInt(rooms.size()));
        }

        while (randomItem.getId() == 70 && !Arrays.asList(5).contains(randomRoom.getRoomID())) {
            randomItem = allItems.get(random.nextInt(allItems.size()));
            randomRoom = rooms.get(random.nextInt(rooms.size()));
        }

        while (randomItem.getId() == 80 && !Arrays.asList(10).contains(randomRoom.getRoomID())) {
            randomItem = allItems.get(random.nextInt(allItems.size()));
            randomRoom = rooms.get(random.nextInt(rooms.size()));
        }

        while (randomItem.getId() == 90 && !Arrays.asList(15).contains(randomRoom.getRoomID())) {
            randomItem = allItems.get(random.nextInt(allItems.size()));
            randomRoom = rooms.get(random.nextInt(rooms.size()));
        }

        //ensure that the item is not placed in the starting room or the final room
        while (randomRoom.getRoomID() == 0 || randomRoom.getRoomID() == 21) {
            randomRoom = rooms.get(random.nextInt(rooms.size()));
        }

        //
        while (randomItem.getId() == 100 && !Arrays.asList(2, 3, 6, 7, 8, 11, 12, 13, 16, 17, 18, 20).contains(randomRoom.getRoomID())) {
            randomItem = allItems.get(random.nextInt(allItems.size()));
            randomRoom = rooms.get(random.nextInt(rooms.size()));
        }



        // Create a new ItemReference for the random item and add it to the random room
        ItemReference randomItemRef = new ItemReference(randomItem.getId(), randomItem.getName(), randomRoom.getRoomID());
        randomRoom.referredItems.put(randomItem.getId(), randomItemRef);

        System.out.println("Placed a random item (" + randomItem.getName() + ") in room: " + randomRoom.getRoomID());
        System.out.println("Item ID: " + randomItem.getId() + ", Name: " + randomItem.getName() + ", Quantity: " + randomItem.getQuantity());
    }

    public int selectCharacter() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your character:");
        System.out.println("For IT majors, type '1'. For Business Majors, type '2'. For Nursing Majors, type '3'.");

        for (Map<String, Object> character : characters) {
            System.out.println(character.get("id") + ". " + character.get("name"));
        }

        while (true) {
            try {
                String input = scanner.nextLine();
                int selectedId = Integer.parseInt(input); // Convert string input to integer

                // Check if the input is a valid character ID
                if (selectedId >= 1 && selectedId <= characters.size()) {
                    return selectedId;
                } else {
                    System.out.println("Invalid input. Please enter a valid character ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
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
    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public void setAttack(double attack) {
        this.attack = attack;
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
        //  List itemInInventory = getInventory().stream().map(ItemReference::getName).collect(Collectors.toList());
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Inventory contains:");
            for (ItemReference itemRef : inventory) {
                Item item = indexedItems.get(itemRef.getIndex());
                System.out.println("Item ID: " + item.getId() + ", Name: " + item.getName() + ", Type: " + item.getIsType() +  ", Description: " + item.getDescription()); // + ", Quantity: " + item.getQuantity()); //FIXME: cannot use getQuantity because would return redundant items
            }
        }
    }

    public void accessMap(){
        int[] test = indexedRooms.get(currentRoom);

        System.out.println(test);

    }

    public Item getEquipped() {
        return equipped;
    }

    public void setEquipped(Item equipped) {
        this.equipped = equipped;
    }
    public Item getItem(int i) {
        return indexedItems.get(i);
    }

    public void consumeStats(Item.Stats incomingStats) {
        hitPoints += incomingStats.hp;
        attack += incomingStats.atk;
        defense += incomingStats.def;
    }


    public void combatMode() {
        gameMode = MODE.BATTLE;

        Actor monster = getMonsterInCurrentRoom();
        System.out.println("You are in combat with " + monster.getName());
        System.out.println("Your HP: " + getHitPoints() + "/" + getMaxHitPoints());
        System.out.println("Monster's HP:" + monster.getHitPoints() + "/" + monster.getMaxHitPoints());
        while (monster.getHitPoints() > 0 && getHitPoints() > 0) {
            Puzzle.PairQA randomPuzzle = puzzle.getRandomPuzzle(Puzzle.topic.valueOf(monster.getType()));

            puzzle.startPuzzleForCombat(monster, randomPuzzle);
            System.out.println(randomPuzzle.isSolved());
            if (randomPuzzle.isSolved()) {
                System.out.println("You attacked the monster!");
                monster.setHitPoints(monster.getHitPoints() - Math.abs(getAttack() - monster.getDefense()));
            } else {
                System.out.println("The monster attacked you");
                setHitPoints(getHitPoints() - Math.abs(monster.getAttack() - getDefense()));
                //TODO: implement Passive of the monsters
            }
            System.out.println("Your HP: " + getHitPoints() + "/" + getMaxHitPoints());
            System.out.println("Monster's HP:" + monster.getHitPoints() + "/" + monster.getMaxHitPoints());
        }
        if (monster.getHitPoints() < 0) {
            removeMonster(); // remove the monster so you don't fight it again
            System.out.println("You have defeated the monster!");
        } else if (getHitPoints() < 0) {
            System.out.println("You will be return to where you last save ");
            //TODO: implement the load here after they lose.
        }


    }
    public void roomPuzzle(){
        Room room = getCurrentRoom();
        Puzzle.PairQA randomPuzzle = puzzle.getRandomPuzzle(Puzzle.topic.valueOf(room.getTopicType()));
        int maxAttempt = room.getPuzzleAttempts();
        if(room.isHasPuzzle()){
            for (int i = maxAttempt; i > 0; i--){
                puzzle.startPuzzleForRoom(room,randomPuzzle);
                if(randomPuzzle.isSolved()){
                    System.out.println("you answered it correct!");
                    room.setHasPuzzle(false);
                    break;
                } else{
                    System.out.println("Wrong! You have " + (i-1) + " attempts left.");
                }
            }
        }
    }

    //DO NOT DELETE OR MODIFY - for list_item()
    public HashMap<Integer, Item> getItems() {
        return indexedItems;
    }
    public HashMap<Integer, Actor> getMonsterList(){
        return indexedMonsters;
    }

    //DO NOT DELETE OR MODIFY - for populateRandomItem()
    public HashMap<Integer, Item> getIndexOfItems() {
        return indexedItems;
    }
}