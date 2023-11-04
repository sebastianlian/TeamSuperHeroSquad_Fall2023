package Model;

public class Item {
    private int index;
    private String name;
    private String description;

    public Item(int index, String name, String description) {
        this.index = index;
        this.name = name;
        this.description = description;
    }

    public int getIndex() { return index; }

    public String getName() { return name; }

    public String getDescription() { return description; }
}
