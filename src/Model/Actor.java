package Model;

import java.io.Serializable;

public class Actor implements Serializable {
    //Class for holding relevant information of every actor in game including the player
    String name, description, type;

    //Stats of actor
    double maxHitPoints, hitPoints, defense, attack;

    //TODO: implement by declaration possible innates (special passives) for the application of each monster

    //TODO: define best way to implement the innates either through setting a passive in the game that hold an idea

    int startingPosition, currentPosition;

    //TODO: IDEA implement a player Actor constrcutor



    public Actor(String name, String description, double hitPoints, double defense, double attack, int startingPosition, String type) {
        this.name = name;
        this.description = description;
        this.hitPoints = maxHitPoints = hitPoints;
        this.defense = defense;
        this.attack = attack;
        this.startingPosition = currentPosition = startingPosition ;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public double getMaxHitPoints() {
        return maxHitPoints;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
        this.startingPosition = currentPosition = startingPosition;
    }

    public void takeDamage(double damageTaken) {
        hitPoints -= damageTaken;
    }

    public boolean hasItemID(int i) {
        return true;
    }
}