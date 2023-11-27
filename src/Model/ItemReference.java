package Model;

import java.io.Serializable;

public class ItemReference implements Serializable {
    private int index;
    private String name;
    private int position;

    //TODO: remove Item. index (id) already matches the id of the Item and are coupled in state
    private Item item; // Reference to an Item object

    public ItemReference(int index, int position) {
        this.index = index;
        this.position = position;
    }

    public ItemReference(Item item) {
        this.index = item.getId();
        this.name = item.getName();
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

    //TODO: implementing this with this design patten to remind myself to refactor
    public void useItem(State state) {
        //This item must be in inventory to be used
        state.consumeStats(state.indexedItems.get(index).stats);
        state.getInventory().remove(this);
    }
}