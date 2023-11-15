package Controller;

import Model.ItemReference;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

        //Testing for single word named enums
        STATS("Stats"),
        INVENTORY("Inventory"),
        MAP("Map"),
        HELP("Help"),
        LIST("List item"),
        PICKUP("Pickup"),
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
        Command command1 = new CommandManager.Command();
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
        List itemInventory = state.getInventory().stream().map(ItemReference::getName).collect(Collectors.toList());
        System.out.println("Items: " + ((itemInventory.isEmpty()) ? "You have no items in your inventory" : itemInventory));
    }
    public void access_map() {

    }
    public void access_help() {
        System.out.println(validCommandSet);
    }
    public void quit(int statusCode) {
        System.exit(statusCode);
    }
    public void save() {

    }

    public void list_item() {

    }
    public void pickup_item() {

    }
    public void use_item() {

    }
    public void equip_item() {

    }
    public void explore() {
//        List itemNames = state.getCurrentRoom().referredItems.values().stream().map(ItemReference::getName).collect(Collectors.toList());
//        System.out.println("Items: " + ((itemNames.isEmpty()) ? "None" : itemNames));
//        List monsterNames = state.getCurrentRoom().referredMonsters.values().stream().map(MonsterReference::getName).collect(Collectors.toList());
//        System.out.println("Monsters: " + ((monsterNames.isEmpty()) ? "None" : monsterNames));

    }
    public void list_monster() {

    }


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
            save();
        }

        public void PICKUP() {
            pickup_item();
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

        public void LIST() {
            list_item();
        }

    }
}
