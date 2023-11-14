package Model;

public class Item {
    private int itemID;
    private String itemName;
    private String itemType;
    private String itemEffect;
    private int quantity;

    public Item(int itemID, String itemName, String itemType, String itemEffect, int quantity) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemEffect = itemEffect;
        this.quantity = quantity;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemEffect() {
        return itemEffect;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setItemID(int itemID) { this.itemID = itemID;}

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setItemEffect(String itemEffect) {
        this.itemEffect = itemEffect;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Method to list items (assumed functionality)
    public void listItems() {
        // Logic to list items (e.g., printing item details to the console)
        System.out.println(itemID + ": " + itemName + " - " + itemEffect + " (Quantity: " + quantity + ")");
    }

    // Method to simulate picking up an item (increment quantity)
    public void pickUp() {
        quantity++;
    }

    // Method to simulate equipping an item (assumed functionality)
    public void equipItem() {
        // Logic to equip an item (details depend on how you want to handle item equipment)
        System.out.println("Equipping: " + itemName);
    }

    // Method to simulate using an item (decrement quantity)
    public void useItem() {
        if (quantity > 0) {
            quantity--;
            // Logic for the item's effect (details depend on the game mechanics)
            System.out.println("Using: " + itemName);
        } else {
            System.out.println("Cannot use " + itemName + ". No more left.");
        }
    }

    // Additional methods for other interactions can be added here
}
