package Model;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String name, effect, description;
    private boolean type;
    private int quantity, number;
    public ItemStats stats;
    private boolean isEquipped;

    // Updated constructor to match the fields from the YAML file
    public Item(int id, String name, boolean type, String effect, String description, int number, ItemStats stats) {
        this(id, name, type, effect, description, number, stats, false);

    }

    public Item(int id, String name, boolean type, String effect, String description, int number) {
        this(id, name, type, effect, description, number, null, false);

    }

//    public Item(int id, String name, boolean type, String effect, String description, ItemStats stats) {
//        this.id = id;
//        this.name = name;
//        this.effect = effect;
//        this.description = description;
//        this.type = type;
//        this.stats = stats;
//    }

    public Item(int id, String name, boolean type, String effect, String description, int number, ItemStats stats, boolean isEquipped) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.description = description;
        this.number = number;
        this.stats = stats;
        this.isEquipped = isEquipped;
    }

    public boolean isEquipped() {
        return isEquipped;
    }

    public void setEquipped(boolean equipped) {
        isEquipped = equipped;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method to list items (assumed functionality)
    public void listItems() {
        // Logic to list items (e.g., printing item details to the console)
        System.out.println(id + ": " + name + " - " + description + " (Quantity: " + quantity + ")");
    }

//    public String getEffect() {
//        return effect;
//    }
//
//    public void setDescription(String description) { this.description = description; }
//
//    public boolean isType() {
//        if (type == true) {
//            System.out.println("Equippable");
//        } else {
//            System.out.println("Usable");
//        }
//        return type;
//    }
//
//    public void getType(boolean type) {
//        this.type = type;
//    }
//
//    public void setType(boolean type) {
//        this.type = type;
//    }
//
//    public Stats getStats() {
//        return stats;
//    }

//    // Method to simulate picking up an item (increment quantity)
//    public void pickUp() {
//        quantity++;
//    }
//
//    // Method to simulate equipping an item (assumed functionality)
//    public void equipItem() {
//        // Logic to equip an item (details depend on how you want to handle item equipment)
//        System.out.println("Equipping: " + name);
//    }
//
//    // Method to simulate using an item (decrement quantity)
//    public void useItem() {
//        if (quantity > 0) {
//            quantity--;
//            // Logic for the item's effect (details depend on the game mechanics)
//            System.out.println("Using: " + name);
//        } else {
//            System.out.println("Cannot use " + name + ". No more left.");
//        }
//    }

    public ItemStats getStats() {
        return stats;
    }

    public String getEffect() {
        return effect;
    }

    public boolean getIsType() {
        return type;
    }

    public int getQuantity() {
        return number;
    }

    public void setQuantity(int number) {
        this.number = number;
    }

    public static class ItemStats extends Model.Stats {
        //Assumes non-dynamic item stats
        protected final double hp;
        protected final double def;
        protected final double atk;

        public ItemStats(double hp, double def, double atk) {
            this.hp = hp;
            this.def = def;
            this.atk = atk;
        }


        public void addTo(Stats stats) {
            stats.hp += this.hp;
            stats.atk += this.atk;
            stats.def += this.def;
        }

        public void subtractFrom(Stats stats) {
            stats.hp -= this.hp;
            stats.atk -= this.atk;
            stats.def -= this.def;
        }

        @Override
        public double getHp() {
            return hp;
        }

        @Override
        public double getDef() {
            return def;
        }

        @Override
        public double getAtk() {
            return atk;
        }
    }
}