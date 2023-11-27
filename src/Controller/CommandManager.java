package Controller;

import Model.Item;
import Model.ItemReference;
import Model.State;
import View.ConsoleTUI;

import java.io.*;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import static Controller.Game.state;

public class CommandManager {
    public String commandEntered = "";
    public String cmdAttrEntered = "";
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

        //Testing for single word named enums
        STATS("Stats"),
        INVENTORY("Inventory"),
        MAP("Map"),
        HELP("Help"),
        LIST("List item"),
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
        commandEntered = expectedCommandInput;
        cmdAttrEntered = expectedCommandAttr;

//        Dead Code
//        Scanner scan = new Scanner(System.in);
//        System.out.println();
//        String userInput = scan.nextLine().toUpperCase();

        try{
            ValidCommand input = ValidCommand.valueOf(expectedCommandInput);
            //stream.map not working: TODO: test to see if necessary
            if (validCommandSet.contains(input) || validCommandSet.stream().map(ValidCommand::getCommandInput).anyMatch(cmdName -> cmdName.equalsIgnoreCase(expectedCommandInput))){
                ConsoleTUI.dotdotdot("Performing \"" + input  + "\""); // TEMP: Checks if the input is holding the command

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
        Method method = Command.class.getMethod(String.valueOf(command).toUpperCase());
        Command command1 = new Command();
        method.invoke(command1);
        resetCommandInput();
    }
    public void resetCommandInput() {
        commandEntered = "";
        cmdAttrEntered = "";
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
//        if (state.getCurrentRoom().isFirstVisit()) {
//            System.out.println("The room seems vaguely familiar...");
//        } else

        if (state.getCurrentRoom().isVisited()) {
            System.out.println("It seems we have been here before...");
        }

//        state.getCurrentRoom().setVisited();
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
//        state.accessMap();
        int[] directions = new int[]{0,1,2,3};
        // 0 = north, 3 = west, 2 =down , 1 = east

        HashMap<Integer, String> map = new HashMap<>();

        map.put(0,"North");
        map.put(1,"East");
        map.put(2,"South");
        map.put(3,"West");

         for(int direct: map.keySet()){
            if(state.getCurrentOutlets()[direct] != -1){
                System.out.println("Way to exit");
                System.out.println("To go to room: " + state.getRoom(state.getCurrentOutlets()[direct]).getRoomName());
                System.out.println("Exit -> "+ map.get(direct));
                System.out.println("----------");
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
        String filePath = "save.txt";

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(state);
        objectOutputStream.close();

    }
    public State load() throws Exception {
        String filePath = "save.txt";

        FileInputStream fileInputStream = new FileInputStream(filePath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (State) objectInputStream.readObject();

    }
    public void drop_item(String cmdAttr){
        System.out.println("Attempting to drop item: " + cmdAttr);

        ItemReference itemReference = itemFromInv(cmdAttr);

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
                System.out.println("Found item: " + itemReference.getName());
                state.moveIntoInventory(itemReference);
                System.out.println("You picked up " + itemReference.getName());
            } else {
                System.out.println("The item is not in the current room.");
            }
        }
    public void use_item(String cmdAttr) {
        ItemReference itemRef = itemFromInv(cmdAttr);
        Item selectItem = state.getItem(itemRef.getIndex());

        if(!selectItem.isType()) {
            itemRef.useItem(state);
        }
    }
    public void equip_item(String cmdAttr) {

        ItemReference itemRef = itemFromInv(cmdAttr);

        if(state.getItem(itemRef.getIndex()).isType()) {
            //Check for equippable now set to equippedItem
            state.equipItem(itemRef);
            state.getInventory().remove(itemRef); //or inventory.remove(itemRef);
        }
    }
    public void explore() {
        List itemsInRoom = state.getCurrentRoom()
                .getReferredItems()
                .values()
                .stream()
                .map(ItemReference::getName)
                .collect(Collectors.toList());
        System.out.println("Items in the Room: " + itemsInRoom);

    }
    public void examine() {



    }
    public void list_monster() {

    }

    //Private helper methods
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

        public void SAVE() throws Exception {
            save();
        }

        public void DROP(){

        }

        public void PICKUP() {
            pickup_item(cmdAttrEntered);
        }

        public void USE() {
            use_item(cmdAttrEntered);
        }

        public void EQUIP() {
            equip_item(cmdAttrEntered);
        }

        public void EXPLORE() {
            explore();
        }

    }
}
