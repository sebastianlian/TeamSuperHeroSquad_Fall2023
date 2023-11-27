package Model;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String name;
    private String effect;
    private String description;
    private boolean type;
    private int quantity;
    public ItemStats stats;


    //TODO: get rid of this constructor when parsing method is corrected
    public Item(int id, String name, boolean type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        //  this.quantity = quantity;
    }


    public Item(int id, String name, boolean type, String description, ItemStats stats) {
        this.id = id;
        this.name = name;
        this.effect = effect;
        this.description = description;
        this.type = type;
        this.stats = stats;
    }

    public int getId() { return id; }


    public void setId(int id) { this.id = id; }


    public String getName() { return name; }


    public void setName(String name) { this.name = name; }



    public boolean isType() { return type; }

    // Method to list items (assumed functionality)
    public void listItems() {
        // Logic to list items (e.g., printing item details to the console)
        System.out.println(id + ": " + name + " - " + description + " (Quantity: " + quantity + ")");
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