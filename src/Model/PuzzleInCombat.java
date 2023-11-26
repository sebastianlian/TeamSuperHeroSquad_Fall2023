package Model;

public class PuzzleInCombat extends Puzzle{
    public PuzzleInCombat(topic puzzletopic) throws Exception {
        super(puzzletopic);
    }

    public void start(Actor monster) {
        topic topicType = topic.valueOf(monster.getType());
        if (validateTopic(topicType)) {
            System.out.println("Combat started with " + monster.getName() + "!");
            getRandomPuzzle(topicType);
        }
    }

    @Override
    public boolean validateTopic(topic inputTopic) {
        return super.validateTopic(inputTopic);
    }
}