package Model;

public class ItemReference {
    private int index;
    private int position;

    public ItemReference(int index, int position) {
        this.index = index;
        this.position = position;
    }

    public int getIndex() { return index; }

    public int getPosition() { return position; }
}
