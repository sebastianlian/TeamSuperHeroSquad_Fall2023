package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
    private int id;
    private String roomName;
    private String roomDescription;
    private boolean checkedRoom;
    private String moveUp;
    private String moveLeft;
    private String moveDown;
    private String moveRight;
    private boolean isVisited;
    private ArrayList<Integer> items;
    protected HashMap<Integer, ItemReference> referredItems = new HashMap<>(); //Integer stands for the item id and the ItemReference refers to the actual item
    private int monsterInRoom;
    private String topicType;
    private int puzzleAttempts;
    private boolean hasPuzzle;

    public Room(int id, String roomName, String roomDescription) {
        this.id = id;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.checkedRoom = false;
        this.isVisited = false;
        this.items = new ArrayList<>();
        //TODO: add ArrayList for monsters/ actors
    }

    public Room(int id, String roomName, String roomDescription, ArrayList<Integer> items, int puzzleAttempts, String topicType, boolean hasPuzzle) {
        this.id = id;
        this.roomName = roomName; // Initialize roomName
        this.roomDescription = roomDescription;
        this.checkedRoom = false;
        this.isVisited = false;
        this.items = (items == null) ? new ArrayList<>() : items;
        this.puzzleAttempts = puzzleAttempts;
        this.topicType = topicType;
        this.hasPuzzle = hasPuzzle;
    }

    public boolean isHasPuzzle(){
        return hasPuzzle;
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


    // Additional methods to handle movements, items, and monster interactions can be added here
}