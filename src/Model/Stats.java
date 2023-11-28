package Model;

public class Stats {
    //Assumes non-dynamic item stats
    protected double hp;
    protected double def;
    protected double atk;

    public Stats() {

    }

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
