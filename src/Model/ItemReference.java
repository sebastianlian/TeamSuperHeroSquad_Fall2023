package Model;

public class ItemReference {
    private int index;
    private String name;
    private int position;

    public ItemReference(int index, int position) {
        this.index = index;
        this.position = position;
    }

    public ItemReference(int index, String name, int position) {
        this.index = index;
        this.name = name;
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
