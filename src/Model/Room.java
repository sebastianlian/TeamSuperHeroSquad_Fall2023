package Model;

import java.util.ArrayList;

public class Room {
    private String name;
    private String description;
    private int number;
    private ArrayList<Integer> items;

    private boolean isVisited = false;

    public Room(int number, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
    }

    public Room(int number, String name, String description, ArrayList<Integer> items) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.items = items;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getNumber() { return number; }

    public ArrayList<Integer> getItems() { return items; }

    public void setVisited(boolean value) {
        this.isVisited = value;
    }
}
