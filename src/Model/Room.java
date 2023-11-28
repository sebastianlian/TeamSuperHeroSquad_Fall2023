package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Room implements Serializable {
    private int id;
    private String roomName;
    private String roomDescription;
    private boolean checkedRoom;
    private boolean isVisited;
    private ArrayList<Integer> items;
    protected HashMap<Integer, ItemReference> referredItems = new HashMap<>(); //Integer stands for the item id and the ItemReference refers to the actual item
    private String topicType;
    private int puzzleAttempts;
    private boolean hasPuzzle;
    private boolean isLocked;

    public Room(int id, String roomName, String roomDescription, ArrayList<Integer> items, int puzzleAttempts, String topicType, boolean hasPuzzle,boolean isLocked) {
        this.id = id;
        this.roomName = roomName; // Initialize roomName
        this.roomDescription = roomDescription;
        this.checkedRoom = false;
        this.isVisited = false;
        this.items = (items == null) ? new ArrayList<>() : items;
        this.puzzleAttempts = puzzleAttempts;
        this.topicType = topicType;
        this.hasPuzzle = hasPuzzle;
        this.isLocked = isLocked;
    }

    public boolean isHasPuzzle(){
        return hasPuzzle;
    }
    public boolean isLocked(){
        return isLocked;
    }
    public void setLocked(boolean isLocked){
        this.isLocked = isLocked;
    }
    public void setHasPuzzle(boolean hasPuzzle) {
        this.hasPuzzle = hasPuzzle;
    }

    public int getPuzzleAttempts() {
        return puzzleAttempts;
    }

    public String getTopicType() {
        return topicType;
    }

    public HashMap<Integer, ItemReference> getReferredItems() {
        return referredItems;
    }

    public int getRoomID() { return id; }

    public String getRoomName() { return roomName; }

    public String getRoomDescription() { return roomDescription; }


    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setVisited() { isVisited = true; }

    public boolean isVisited() {
        return isVisited;
    }

}