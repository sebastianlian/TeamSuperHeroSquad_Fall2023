package Model;

public class Item {
    private int id;
    private String name;
    private boolean type; // true for equippable, false for consumable
    private String effect; // Include the effect field
    private String description;
    private int number; // Include the number field
    public Stats stats;
    //public?
    private boolean isEquipped;

    // Updated constructor to match the fields from the YAML file
    public Item(int id, String name, boolean type, String effect, String description, int number, Stats stats) {
        this(id, name, true, effect, description, number, stats, false);

    }
    public Item(int id, String name, boolean type, String effect, String description, int number, Stats stats, boolean isEquipped) {
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

    public Item(int id, String name, String effect, String description, boolean type, Stats stats) {
        this.id = id;
        this.name = name;
        this.effect = effect;
        this.description = description;
        this.type = type;
        this.quantity = quantity;
        this.stats = stats;
    }

    public int getId() { return id; }

    public String getName() { return name; }


    public void setName(String name) { this.name = name; }



    public boolean isType() { return type; }

    // Method to list items (assumed functionality)
    public void listItems() {
        // Logic to list items (e.g., printing item details to the console)
        System.out.println(id + ": " + name + " - " + description + " (Quantity: " + quantity + ")");
    }

    public Stats getStats() {
        return stats;
    }
    public String getDescription() { return description; }

    public String getEffect() {
        return effect;
    }

    // Stats inner class
    public static class Stats {
        private final double hp;
        private final double def;
        private final double atk;

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