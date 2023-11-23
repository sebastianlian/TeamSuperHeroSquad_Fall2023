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

    public void randomizedPuzzle(){
        int random = (int) (Math.random() * 9);
        switch (random){
            case 0:
                this.puzzletopic = topic.CHEM;
                break;
            case 1:
                this.puzzletopic = topic.HIST;
                break;
            case 2:
                this.puzzletopic = topic.HEALTH;
                break;
            case 3:
                this.puzzletopic = topic.MATH;
                break;
            case 4:
                this.puzzletopic = topic.IT;
                break;
            case 5:
                this.puzzletopic = topic.ENGL;
                break;
            case 6:
                this.puzzletopic = topic.BUSN;
                break;
            case 7:
                this.puzzletopic = topic.NURS;
                break;
            case 8:
                this.puzzletopic = topic.FINAL;
                break;
        }
    }

}