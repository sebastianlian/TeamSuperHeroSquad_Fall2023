package Model;

public class Item {
    private int id;
    private String name, effect, description;
    private boolean type;
    private int quantity;
    protected Stats stats;


    //TODO: get rid of this constructor when parsing method is corrected
    public Item(int id, String name, boolean type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
      //  this.quantity = quantity;
    }


    public Item(int id, String name, String effect, String description, boolean type, Stats stats) {
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

  
    public void setType(boolean type) { this.type = type; }

  
    public String getDescription() { return description; }

  
    public void setDescription(String description) { this.description = description; }

  
    public int getQuantity() { return quantity; }

  
    public void setQuantity(int quantity) { this.quantity = quantity; }

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


    public static class Stats {
        //Assumes non-dynamic item stats
        protected final double hp;
        protected final double def;
        protected final double atk;

        public Stats(double hp, double def, double atk) {
            this.hp = hp;
            this.def = def;
            this.atk = atk;
        }

        public double getHp() {
            return hp;
        }

        public double getDef() {
            return def;
        }

        public double getAtk() {
            return atk;
        }
    }
}
