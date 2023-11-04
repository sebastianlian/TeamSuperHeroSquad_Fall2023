package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class State {
    // Statics
    private HashMap<Integer, Item> itemIndex;

    // Game State
    private HashMap<Room, int[]> rooms;

    // Player Variables
    private ArrayList<ItemReference> inventory;
    private int position = 0; // FIXME: There must be a 0th room.

    public State(Callable<HashMap<Integer, Item>> populateItemIndex, Callable<HashMap<Room, int[]>> populateRooms) throws Exception {
        inventory = new ArrayList<>();

        itemIndex = populateItemIndex.call();
        System.out.println("[DEBUG] Indexed Items: " + itemIndex.size());

        rooms = populateRooms.call();
        System.out.println("[DEBUG] Indexed Rooms: " + rooms.size());
    }
}
