package Model;

public class ItemReference {
    // GlobalID in reference to id in items.yaml file
    private int globalID;
    private String name;
    private int position;

    public ItemReference(int globalID, int position) {
        this.globalID = globalID;
        this.position = position;
    }

    public ItemReference(int globalID, String name, int position) {
        this.globalID = globalID;
        this.name = name;
        this.position = position;
    }


    public int getGlobalID() { return globalID; }

    public String getName() {
        return name;
    }

    public int getPosition() { return position; }
}
