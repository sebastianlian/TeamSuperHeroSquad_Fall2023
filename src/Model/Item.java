package Model;

public class Item {
    private int id;
    private String name;
    private String type;
    private String effect;
    private int quantity;

    public Item(int id, String name, String type, String effect, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.quantity = quantity;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getItemType() {
        return itemType;
    }

    public String getEffect() {
        return effect;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItemID(int itemID) { this.itemID = itemID;}

    public void setName(String name) {
        this.name = name;
    }

    public void setItemType(boolean itemType) {
        this.itemType = itemType;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to list items (assumed functionality)
    public void listItems() {
        // Logic to list items (e.g., printing item details to the console)
        System.out.println(id + ": " + name + " - " + effect + " (Quantity: " + quantity + ")");
    }

    // Method to simulate picking up an item (increment quantity)
    public void pickUp() {
        quantity++;
    }

    // Method to simulate equipping an item (assumed functionality)
    public void equipItem() {
        // Logic to equip an item (details depend on how you want to handle item equipment)
        System.out.println("Equipping: " + name);
    }

    // Method to simulate using an item (decrement quantity)
    public void useItem() {
        if (quantity > 0) {
            quantity--;
            // Logic for the item's effect (details depend on the game mechanics)
            System.out.println("Using: " + name);
        } else {
            System.out.println("Cannot use " + name + ". No more left.");
        }
    }

    // Additional methods for other interactions can be added here
}
