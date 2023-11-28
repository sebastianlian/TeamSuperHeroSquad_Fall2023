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


    public ItemStats getStats() {
        return stats;
    }

    public String getEffect() {
        return effect;
    }

    public boolean getIsType() {
        return type;
    }

    public String getQuantity() {
        return String.valueOf(number);
    }

    public static class ItemStats extends Model.Stats implements Serializable {
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