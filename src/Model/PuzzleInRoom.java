package Model;

public class PuzzleInRoom extends Puzzle{
    private int attempts;
    public PuzzleInRoom(Puzzle.topic puzzleTopic, String question, String answer, boolean isSolved, int attempts){

        super(puzzleTopic, question, answer, isSolved);
        this.attempts = attempts;
    }
}
