package Model;

public class PuzzleInRoom extends Puzzle {
    private int attempts;
    public PuzzleInRoom(topic puzzletopic, int attempts) throws Exception {
        super(puzzletopic);
        this.attempts = attempts;
    }
}