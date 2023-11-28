package Model;

import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Puzzle {

    public enum topic {
        Chemisty, History, Health, Math, IT, English, Business, Nursing, Final, All
    }

    private topic currentPuzzleTopic;
    private String currentQuestion;
    private String currentAnswer;
    private HashMap<String, ArrayList<PairQA>> QAMap;

    public Puzzle(topic puzzletopic) throws Exception {
        QAMap = parsePuzzle();
    }

    public HashMap<String, ArrayList<PairQA>> parsePuzzle() throws Exception {

        Yaml yaml = new Yaml();

        // List of monsters.
        HashMap<String, ArrayList<PairQA>> allPuzzles = new HashMap<>();
        String source = Files.readString(Paths.get("puzzles.yaml"));

        Map<String, Object> object = yaml.load(source);

        LinkedHashMap<Object, ArrayList<Map.Entry<Object, Object>>> objects_test = (LinkedHashMap<Object, ArrayList<Map.Entry<Object, Object>>>) object.get("Puzzles");

        for (Object topicEntries : objects_test.entrySet()) {
            ArrayList<PairQA> puzzleEntries = new ArrayList<>();
            //NOTE-TODO: Look about if Map.Entry is code smell, minimal readability
            Map.Entry<Object, Object> topicMapping = (Map.Entry<Object, Object>) topicEntries;
            String topicString = String.valueOf(topicMapping.getKey());

            for (Object puzzle :
                    objects_test.get(((Map.Entry<?, ?>) topicEntries).getKey())) {
                Map<Object, Object> puzzleMapping = (Map<Object, Object>) puzzle;
                PairQA pairQA = mapToPairQA((String) puzzleMapping.get("question"),
                        (String) puzzleMapping.get("answer"));
                puzzleEntries.add(pairQA);
//                System.out.println(mapping.get("question"));
//                System.out.println(mapping.get("answer"));
            }

            allPuzzles.put(topicString, puzzleEntries);
        }

        return allPuzzles;
    }

    //Getter & Setters

    public PairQA getRandomPuzzle(topic puzzleTopic) {
        ArrayList<PairQA> pairQAS = QAMap.get(puzzleTopic.name());
        int puzzleQuestions = pairQAS.size();
        Random rand = new Random();
        int randomRoll = rand.nextInt(puzzleQuestions);

        return pairQAS.get(randomRoll);
    }

    //Private methods

    //Method to map inputs to a PairQA
    private PairQA mapToPairQA(String question, String answer) {
        return new PairQA(question, answer);
    }
    private PairQA mapToPairQA(Object question, Object answer) {
        return new PairQA(String.valueOf(question), String.valueOf(answer));
    }

//    private PairQA mapToPairQA(Map.Entry<String, String> entry) {
//        return new PairQA(entry);
//    }
    private PairQA mapToPairQA(Map.Entry<Object, Object> entry) {
        Map.Entry<String, String> stringEntry = Map.Entry.class.cast(entry);
        return new PairQA(stringEntry);
    }

//    public void randomizedPuzzle(){
//        int random = (int) (Math.random() * 9);
//        switch (random){
//            case 0:
//                this.puzzletopic = topic.CHEM;
//                break;
//            case 1:
//                this.puzzletopic = topic.HIST;
//                break;
//            case 2:
//                this.puzzletopic = topic.HEALTH;
//                break;
//            case 3:
//                this.puzzletopic = topic.MATH;
//                break;
//            case 4:
//                this.puzzletopic = topic.IT;
//                break;
//            case 5:
//                this.puzzletopic = topic.ENGL;
//                break;
//            case 6:
//                this.puzzletopic = topic.BUSN;
//                break;
//            case 7:
//                this.puzzletopic = topic.NURS;
//                break;
//            case 8:
//                this.puzzletopic = topic.FINAL;
//                break;
//        }
//    }

    public class PairQA { //NOTE: could make final, probably bad idea
        private final String question;
        private final String answer;

        private boolean isSolved;
        public PairQA(String question, String answer) { this.question = question; this.answer = answer;}

        public PairQA(Map.Entry<String, String> qaEntry) {this.question = qaEntry.getKey(); this.answer = qaEntry.getValue();}

        //Getters & Setters for PairQA

        public String getQuestion() {
            return question;
        }
        public String getAnswer() {
            return answer;
        }
        public void setSolved(boolean solved) {
            isSolved = solved;
        }

        public void setSolved() {
            isSolved = true;
        }

        public boolean isSolved() {
            return isSolved;
        }

    }

}
