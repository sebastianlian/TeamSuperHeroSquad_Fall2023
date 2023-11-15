package Model;

public class Puzzle {

    public enum topic{
        CHEM, HIST, HEALTH, MATH, IT, ENGL, BUSN, NURS, FINAL
    }
    private topic puzzletopic;
    private String question;
    private String answer;
    private boolean isSolved;

    public Puzzle(topic puzzletopic, String question, String answer, boolean isSolved){
        this.puzzletopic = puzzletopic;
        this.question = question;
        this.answer = answer;
        this.isSolved = isSolved;
    }

    public topic getPuzzletopic() {
        return puzzletopic;
    }
    public void setPuzzletopic(topic puzzletopic) {
        this.puzzletopic = puzzletopic;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setSolved(boolean solved) {
        isSolved = solved;
    }
    public boolean isSolved() {
        return isSolved;
    }


}
