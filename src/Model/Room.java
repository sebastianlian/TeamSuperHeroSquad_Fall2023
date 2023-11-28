package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Room implements Serializable {
    private int id;
    private String roomName;
    private String roomDescription;
    private boolean checkedRoom;
    private String moveUp;
    private String moveLeft;
    private String moveDown;
    private String moveRight;
    private boolean firstVisit;
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
        this.monsterInRoom = 0;
    }

    public Room(int id, String roomName, String roomDescription, ArrayList<Integer> items ) {
        this.id = id;
        this.roomName = roomName; // Initialize roomName
        this.roomDescription = roomDescription;
        this.checkedRoom = false;
        this.isVisited = false;
        this.items = (items == null) ? new ArrayList<>() : items;
        this.monsterInRoom = 0;
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

    public void setReferredItems(HashMap<Integer, ItemReference> referredItems) {
        this.referredItems = referredItems;
    }

    public int getRoomID() { return id; }

    public String getRoomName() { return roomName; }

    public String getRoomDescription() { return roomDescription; }

    public boolean isCheckedRoom() { return checkedRoom; }

    public String getMoveUp() { return moveUp; }

    public String getMoveLeft() { return moveLeft; }

    public String getMoveDown() { return moveDown; }

    public String getMoveRight() { return moveRight; }


    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setVisited() { isVisited = true; }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }

    public boolean isVisited() {
        return isVisited;
    }


    public void setMonsterInRoom(int monsterInRoom) { this.monsterInRoom = monsterInRoom; }

    // Other methods
    public void explore() {
        // Implementation for exploring the room
        System.out.println("Room: " + roomName); // Display room name
        System.out.println(roomDescription);
        for (Integer item : items) {
            //Meant for printing items
        }
        // Additional logic for monsters, checking the room, etc.
    }

    // Additional methods to handle movements, items, and monster interactions can be added here
}