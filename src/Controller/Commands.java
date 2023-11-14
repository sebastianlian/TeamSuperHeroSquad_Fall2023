package Controller;

import java.util.EnumSet;
import java.util.Scanner;

public class Commands {
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
        LIST_MONSTER("List monster");

        final String commandInput;
        ValidCommand(String commandInput){
            this.commandInput = commandInput;
        }

        public String getCommandInput() {
            return commandInput;
        }
    }


    private final static EnumSet<ValidCommand> validCommandSet = EnumSet.allOf(ValidCommand.class);


    public void validatingCommands(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a command: ");
        String user = scan.nextLine().toUpperCase();

        try{
        ValidCommand input = ValidCommand.valueOf(user);
            if (validCommandSet.contains(input)){
                System.out.println("You performed " + input); // TEMP: Checks if the input is holding the command
            }
        } catch(IllegalArgumentException e) {
            System.out.println("Invalid output.Please Enter a Valid command");
        }
    }
}
