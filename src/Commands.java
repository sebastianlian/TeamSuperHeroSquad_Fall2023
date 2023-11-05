import java.util.EnumSet;

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

    ValidCommand input;
    EnumSet<ValidCommand> validCommandSet = EnumSet.allOf(ValidCommand.class);
    public void validatingCommands(){

        switch(input){
            case MOVE -> {}
            case SHOW_STATS ->{}
            case OPEN_INVENTORY -> {}
            case ACCESS_MAP -> {}
            case ACCESS_HELP -> {}
            case QUIT -> {}
            case SAVE -> {}
            case LIST_ITEM -> {}
            case PICKUP_ITEM -> {}
            case USE_ITEM -> {}
            case EQUIP_ITEM -> {}
            case EXPLORE -> {}
            case LIST_MONSTER -> {}
        }
    }
}
