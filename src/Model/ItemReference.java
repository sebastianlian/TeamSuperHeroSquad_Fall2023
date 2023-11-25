package Model;

public class ItemReference {
    private int index;
    private String name;
    private int position;
    private Item item; // Reference to an Item object


    public ItemReference(int index, int position) {
        this.index = index;
        this.position = position;
    }

    public ItemReference(int index, String name, int position, Item item) {
        this.index = index;
        this.name = name;
        this.position = position;
        this.item = item;
    }

    public ItemReference(int index, String name, int position) {
        this.index = index;
        this.name = name;
        this.position = position;
    }


    public int getIndex() { return index; }

    public String getName() {
        return name;
    }

    public int getPosition() { return position; }
}