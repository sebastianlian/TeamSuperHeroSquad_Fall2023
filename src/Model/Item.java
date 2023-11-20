package Model;

public class Item {
    private int id;
    private String name;
    private boolean type;
    private String effect;
    private String description;
    private int quantity;
    protected Stats stats;


    //TODO: get rid of this constructor when parsing method is corrected
    public Item(int id, String name, boolean type, String effect, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.description = description;
//        this.quantity = quantity;
    }


    public Item(int id, String name, String effect, String description, int quantity, boolean type, Stats stats) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public boolean isType() {
        if (type == true) {
            System.out.println("Equippable");
        } else {
            System.out.println("Usable");
        }
        return type;
    }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Stats getStats() {
        return stats;
    }

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