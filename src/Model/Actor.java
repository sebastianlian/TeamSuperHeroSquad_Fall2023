package Model;

public class Actor {
    //Class for holding relevant information of every actor in game including the player
    String name, description;

    //Stats of actor
    double maxHitPoints,hitPoints, defense, attack;

    //TODO: implement by declaration possible innates (special passives) for the application of each monster

    //TODO: define best way to implement the innates either through setting a passive in the game that hold an idea

    int startingPosition, currentPosition;

    //TODO: IDEA implement a player Actor constrcutor



    public Actor(String name, String description, double hitPoints, double defense, double attack, int startingPosition) {
        this.name = name;
        this.description = description;
        this.hitPoints = hitPoints;
        this.defense = defense;
        this.attack = attack;
        this.startingPosition = startingPosition = currentPosition;
    }


    public void takeDamage(double damageTaken) {
        hitPoints -= damageTaken;
    }

}