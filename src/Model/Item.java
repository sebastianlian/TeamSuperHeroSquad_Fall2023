package Model;

public class Item {
    private int id;
    private String name;
    private boolean type; // true for equippable, false for consumable
    private String effect; // Include the effect field
    private String description;
    private int number; // Include the number field
    protected Stats stats;
    private boolean isEquipped;

    // Updated constructor to match the fields from the YAML file
    public Item(int id, String name, boolean type, String effect, String description, int number, Stats stats) {
        this(id, name, type, effect, description, number, stats, false);

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

    public Stats getStats() {
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

    // Stats inner class
    public static class Stats {
        public final double hp;
        public  final double def;
        public  final double atk;

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