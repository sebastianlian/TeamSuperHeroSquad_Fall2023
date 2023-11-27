package Controller;

import Model.Item;
import Model.ItemReference;
import Model.Room;
import Model.Actor;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import static Controller.Game.state;

public class CommandManager {
    public enum ValidCommand {
        MOVE("Move"),
        SHOW_STATS("Stats"),
        OPEN_INVENTORY("Inventory"),
        ACCESS_MAP("Map"),
        ACCESS_HELP("Help"),
        QUIT("Quit"),
        SAVE("Save"),
        LIST_ITEM("List item"),
        PICKUP_ITEM("Pickup"),
        USE_ITEM("Use"),
        EQUIP_ITEM("Equip"),
        EXPLORE("Explore"),
        LIST_MONSTER("List monster"),
        UNEQUIP("Unequip"),

        //Testing for single word named enums
        STATS("Stats"),
        INVENTORY("Inventory"),
        MAP("Map"),
        HELP("Help"),
        LIST("List"),
        PICKUP("Pickup"),
        DROP("Drop"),
        USE("Use"),
        EQUIP("Equip");
//        LIST_MONSTER("List monster");

        final String commandInput;
        ValidCommand(String commandInput){
            this.commandInput = commandInput;
        }

        public String getCommandInput() {
            return commandInput;
        }
    }


    private final static EnumSet<ValidCommand> validCommandSet = EnumSet.allOf(ValidCommand.class);


    public void validateCommand(String expectedCommandInput, String expectedCommandAttr){
//        Dead Code
//        Scanner scan = new Scanner(System.in);
//        System.out.println();
//        String userInput = scan.nextLine().toUpperCase();

        try{
            ValidCommand input = ValidCommand.valueOf(expectedCommandInput);
            //stream.map not working: TODO: test to see if necessary
            if (validCommandSet.contains(input) || validCommandSet.stream().map(ValidCommand::getCommandInput).anyMatch(cmdName -> cmdName.equalsIgnoreCase(expectedCommandInput))){
                System.out.println("You performed " + input); // TEMP: Checks if the input is holding the command

                runCommand(input.commandInput);

//                Dead Code
//                ExecutorService executor = Executors.newCachedThreadPool();
//                Future<Integer> task1 = executor.submit(callable1);
            }
        } catch(IllegalArgumentException e) {
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public void instCommandCall(String input) {
//        Callable callable =
//    }

    public void runCommand(String command) throws Exception {
        System.out.println(String.valueOf(command));
        Method method = Command.class.getMethod(String.valueOf(command).toUpperCase());
        Command command1 = new Command();
        method.invoke(command1);
    }

    //TODO: implement checkMode() or isMode()
//    public void checkMode() {}


    //TODO: implement each command method/actions here
    //TODO: match Command with mode and check

    public void move(int direction) {
        if (state.getCurrentOutlets()[direction] == -1) {
            System.out.println("You cant go that way!");
            return;
        }
        System.out.print("Moving to a new room... ");
//        TimeUnit.NANOSECONDS.sleep(1000);
        System.out.println("Arrived within " + state.getRoom(state.getCurrentOutlets()[direction]).getRoomName()); // + ((state.getCurrentRoom().isVisited) ? ". Seems familiar..." : ""));

        state.setCurrentRoom(state.getCurrentOutlets()[direction]);
        state.getCurrentRoom().setVisited();
        System.out.println(state.getCurrentRoom().getRoomDescription());

    }
    public void show_stats() {
        System.out.println(" {Player health} : " + (state.getHitPoints()));
        System.out.println(" {Player atk} : " + (state.getAttack()));
        System.out.println(" {Player defense} : " + (state.getDefense()));
    }
    public void open_inventory() {
        //Print inventory if not empty - Print "You didn’t pickup any items yet" if empty
        //List itemInventory = state.getInventory().stream().map(ItemReference::getName).collect(Collectors.toList());
        //System.out.println("Items: " + ((itemInventory.isEmpty()) ? "You have no items in your inventory" : itemInventory));
        state.displayInventory();
    }
    public void access_map() {
        Room currentRoom = state.getCurrentRoom();
        int[] currentRoomOutlets = state.getCurrentOutlets();

        // Displaying the current room
        System.out.println("Current Room: " + currentRoom.getRoomName());

        // Displaying available exits
        System.out.println("Exits:");
        String[] directions = {"North", "East", "South", "West"};
        for (int i = 0; i < currentRoomOutlets.length; i++) {
            if (currentRoomOutlets[i] != -1) {
                Room nextRoom = state.getRoom(currentRoomOutlets[i]);
                System.out.println(directions[i] + " -> " + nextRoom.getRoomName());
            }
        }

        // Displaying visited rooms
        System.out.println("Visited Rooms:");
        for (Room room : state.getIndexedRooms().keySet()) {
            if (room.isVisited()) {
                System.out.println(room.getRoomName());
            }
        }
    }
    public void access_help() {
        System.out.println(validCommandSet);
    }
    public void quit(int statusCode) {
        System.exit(statusCode);
    }
    public void save() throws Exception {
        String filePath = "src/test/resources/store/save.txt";

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(state);

    }
    //SEBASTIAN: DO NOT MODIFY THIS METHOD
    public void list(String cmdAttr) {
        HashMap<Integer, Item> allItems = state.getItems();

        if (cmdAttr.equalsIgnoreCase("items")) {
            if (allItems.isEmpty()) {
                System.out.println("No items found.");
            } else {
                System.out.println("__________________________________________________________");
                System.out.println("All items:");
                for (Item item : allItems.values()) {
                    System.out.println("Item ID: " + item.getId());
                    System.out.println("Name: " + item.getName());
                    System.out.println("Effect: " + item.getEffect());
                    System.out.println("Description: " + item.getDescription());
                    System.out.println("__________________________________________________________");
                }
            }
        } else if(cmdAttr.equalsIgnoreCase("monsters")) {
            HashMap<Integer, Actor> allMonster = state.getMonsterList();

            if (allMonster.isEmpty()) {
                System.out.println("No Monster found.");
            } else {

                System.out.println("All monsters:");
                for (Actor monster : allMonster.values()) {
                    System.out.println("__________________________________________________________");
                    System.out.println("Name : " + monster.getName());
                    System.out.println("Hp: " + monster.getMaxHitPoints());
                    System.out.println("Atk: " + monster.getAttack());
                    System.out.println("Def: " + monster.getDefense());
                    System.out.println("Passive:");
                    System.out.println("__________________________________________________________");
                }
            }
        } else{
            System.out.println("Type 'list items' or 'list monsters'");
        }
    }

    public void drop_item(String cmdAttr){
        System.out.println("Attempting to drop item: " + cmdAttr);

        ItemReference itemReference = state.getInventory()
                .stream()
                .filter(itemRef -> itemRef.getName().equalsIgnoreCase(cmdAttr))
                .findFirst()
                .orElse(null);

        if (itemReference != null) {
            System.out.println("Found item: " + itemReference.getName());
            state.moveFromInventory(itemReference);
            System.out.println("You dropped " + itemReference.getName());
        } else {
            System.out.println("The item is not in your inventory.");
        }
    }

    public void pickup_item(String cmdAttr) {
        System.out.println("Attempting to pick up item: " + cmdAttr);

        ItemReference itemReference = state.getCurrentRoom()
                .getReferredItems()
                .values()
                .stream()
                .filter(itemRef -> itemRef.getName().equalsIgnoreCase(cmdAttr))
                .findFirst()
                .orElse(null);

        if (itemReference != null) {
            state.moveIntoInventory(itemReference);
            System.out.println("You picked up " + itemReference.getName());
        } else {
            System.out.println("The item is not in the current room.");
        }
    }

    public void use_item() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the name of the item to use: ");
        String itemName = scan.nextLine();

        ItemReference itemRef = state.getInventory()
                .stream()
                .filter(item -> item.getName().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);

        if (itemRef != null) {
            Item item = itemRef.getItem();
            if (item != null && !item.isType()) { // Check if the item is consumable
                // Apply the effect of the item
                if (item.getStats() != null && item.getStats().getHp() > 0) {
                    // Assuming you have a method in State to update HP
                    double healingAmount = item.getStats().getHp();
                    state.healPlayer(healingAmount);
                    System.out.println("Used " + item.getName() + ": Healed " + healingAmount + " HP.");
                } else {
                    System.out.println("The item " + item.getName() + " has no usable effect.");
                }
            } else {
                System.out.println("The item " + item.getName() + " is not usable. It might be an equippable item or null.");
            }
        } else {
            System.out.println("The item " + itemName + " is not in your inventory.");
        }
    }

    private void applyItemEffect(Item item) {
        // Check if the item has a healing effect
        if (item.getStats() != null && item.getStats().getHp() > 0) {
            double healingAmount = item.getStats().getHp();

            // Apply the healing
            double newHp = state.getHitPoints() + healingAmount;

            // Ensure HP does not exceed maximum HP, assuming 100 is max HP
            newHp = Math.min(newHp, 100);

            // Set the new HP
            state.setHitPoints(newHp);

            System.out.println("Healed " + healingAmount + " HP. Current HP: " + newHp);
        } else {
            // Handle other types of effects if any
            System.out.println("The item " + item.getName() + " has no healing effect.");
        }
    }


    public void equip_item() {
        Scanner scan = new Scanner(System.in);
        state.displayInventory();
        System.out.println("Enter the name of the item to equip: ");
        String itemName = scan.nextLine().trim();  // Trim to remove leading/trailing spaces

        ItemReference itemRef = state.getInventory()
                .stream()
                .filter(item -> item.getName().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);

        if (itemRef != null) {
            Item item = itemRef.getItem();
            if (item != null && item.isType()) { // Check if item is equippable and not null
                equipItemEffect(item); // Assuming this method sets the equipped item in the player state
//                System.out.println("Equipped " + item.getName() + ": " + item.getEffect());
                state.setEquipped(item);
                state.getInventory().remove(itemRef);
            } else if (item != null) {
                System.out.println("Item " + item.getName() + " is not equippable. It's a consumable item.");
            } else {
                System.out.println("Failed to retrieve item details.");
            }
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    public void unequip(){
        ItemReference itemRef = new ItemReference(state.getEquipped().getId(), state.getEquipped().getName(), state.getCurrentRoom().getRoomID(), state.getEquipped());
        state.getInventory().add(itemRef);

        Item.Stats itemStats = state.getEquipped().getStats();

        if (itemStats != null) {
            // Apply the stats of the item to the player
            double newHp = state.getHitPoints() - itemStats.getHp();
            double newAtk = state.getAttack() - itemStats.getAtk();
            double newDef = state.getDefense() - itemStats.getDef();

            // Update the player's stats
            state.setHitPoints(newHp);
            state.setAttack(newAtk);
            state.setDefense(newDef);

            System.out.println("Equipped " + state.getEquipped().getName() + ". New Stats - HP: " + newHp + ", ATK: " + newAtk + ", DEF: " + newDef);
        } else {
            System.out.println("The item " + state.getEquipped().getName() + " has no equippable stats.");
        }

        state.setEquipped(null);


    }


    private void equipItemEffect(Item item) {
        // Assuming the Stats class has hp, atk, and def as attributes
        Item.Stats itemStats = item.getStats();

        if (itemStats != null) {
            // Apply the stats of the item to the player
            double newHp = state.getHitPoints() + itemStats.getHp();
            double newAtk = state.getAttack() + itemStats.getAtk();
            double newDef = state.getDefense() + itemStats.getDef();

            // Update the player's stats
            state.setHitPoints(newHp);
            state.setAttack(newAtk);
            state.setDefense(newDef);

            System.out.println("Equipped " + item.getName() + ". New Stats - HP: " + newHp + ", ATK: " + newAtk + ", DEF: " + newDef);
        } else {
            System.out.println("The item " + item.getName() + " has no equippable stats.");
        }
    }



    public void explore() {
        List itemsInRoom = state.getCurrentRoom().getReferredItems().values().stream().map(ItemReference::getName).collect(Collectors.toList());
        Actor monster = state.getMonsterInCurrentRoom();
        Room room = state.getCurrentRoom();

        // Does checks for any aspect of using explore, item, puzzle, and monster detection
        if (!itemsInRoom.isEmpty()) {
            System.out.println("Items in the Room: " + itemsInRoom);
        } else{
            System.out.println("No items are in the room.");
        }
        if (room.isHasPuzzle()){
            System.out.println("You encounter a puzzle");
            state.roomPuzzle();
        }else{
            System.out.println("No puzzles are in the room");
        }
        if (monster!=null) {
            System.out.println("A monster approached your presence..." + monster.getName());
            state.combatMode();
        } else {
            System.out.println("No monsters are in the room.");
        }
    }

    public void examine() {



    }
    public void list_monster() {

    }

    //Private methods
    private ItemReference itemFromInv(String itemName) {
        ItemReference itemReference = state.getInventory()
                .stream()
                .filter(itemRef -> itemRef.getName().equalsIgnoreCase(itemName))
                .findFirst()
                .orElse(null);

        return itemReference;
    }

//    private ArrayList<ItemReference> itemsInRoom(String itemName) {
//        ArrayList itemsInRoom = state.getCurrentRoom()
//                .getReferredItems()
//                .values()
//                .stream().;
//
//        return itemsInRoom;
//    }


    public class Command {
        //TEMP class full of methods to pass once able
        public void MOVE() {

        }

        public void STATS() {
            show_stats();
        }

        public void INVENTORY() {
            open_inventory();
        }

        public void MAP() {
            access_map();
        }

        public void HELP() {
            access_help();
        }

        public void QUIT() {
            quit(0);
        }

        public void SAVE() {
//            save();
        }

        public void DROP(){

        }

        public void PICKUP(String cmdAttr) {
            pickup_item(cmdAttr);
        }

        public void USE() {
            use_item();
        }

        public void EQUIP() {
            equip_item();
        }

        public void EXPLORE() {
            explore();
        }

        public void UNEQUIP() {
            unequip();
        }
        public void LIST(String cmdAttr) {
            list(cmdAttr);
        }

    }
}