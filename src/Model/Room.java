package Model;

import java.util.ArrayList;

public class Room {
    private int roomID;
    private String roomName;
    private String roomDescription;
    private boolean checkedRoom;
    private String moveUp;
    private String moveLeft;
    private String moveDown;
    private String moveRight;
    private boolean isVisited;
    private ArrayList<Integer> items;
    private int monsterInRoom;

    public Room(int roomID, String roomName, String roomDescription) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.checkedRoom = false;
        this.isVisited = false;
        this.items = new ArrayList<>();
        //TODO: add ArrayList for monsters/ actors
        this.monsterInRoom = 0;
    }

    public Room(int roomID, String roomName, String roomDescription, ArrayList<Integer> items ) {
        this.roomID = roomID;
        this.roomName = roomName; // Initialize roomName
        this.roomDescription = roomDescription;
        this.checkedRoom = false;
        this.isVisited = false;
        this.items = items;
        this.monsterInRoom = 0;
    }

    public int getRoomID() { return roomID; }

    public String getRoomName() { return roomName; }

    public String getRoomDescription() { return roomDescription; }

    public boolean isCheckedRoom() { return checkedRoom; }

    public String getMoveUp() { return moveUp; }

    public String getMoveLeft() { return moveLeft; }

    public String getMoveDown() { return moveDown; }

    public String getMoveRight() { return moveRight; }

    public boolean isVisited() { return isVisited; }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public int getMonsterInRoom() { return monsterInRoom; }

    public void setRoomID(int roomID) { this.roomID = roomID; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public void setRoomDescription(String roomDescription) { this.roomDescription = roomDescription; }

    public void setCheckedRoom(boolean checkedRoom) { this.checkedRoom = checkedRoom; }

    public void setMoveUp(String moveUp) { this.moveUp = moveUp; }

    public void setMoveLeft(String moveLeft) { this.moveLeft = moveLeft; }

    public void setMoveDown(String moveDown) { this.moveDown = moveDown; }

    public void setMoveRight(String moveRight) { this.moveRight = moveRight; }

    public void setVisited(boolean visited) { isVisited = visited; }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
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




