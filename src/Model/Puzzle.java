package Model;

import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Puzzle {

    public enum topic {
        Chemistry, History, Health, Math, IT, English, Business, Nursing, Final, All
    }
    private HashMap<String, ArrayList<PairQA>> QAMap;

    public Puzzle(topic puzzletopic) throws Exception {
        QAMap = parsePuzzle();
    }
    public Puzzle() throws Exception {
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
//    private PairQA mapToPairQA(Object question, Object answer) {
//        return new PairQA(String.valueOf(question), String.valueOf(answer));
//    }
//
//        private PairQA mapToPairQA(Map.Entry<String, String> entry) {
//        return new PairQA(entry);
//    }
//
//    private PairQA mapToPairQA(Map.Entry<Object, Object> entry) {
//        Map.Entry<String, String> stringEntry = Map.Entry.class.cast(entry);
//        return new PairQA(stringEntry);
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
        public void setSolved() {
            isSolved = true;
        }

        public void setSolved(boolean solved) {
            isSolved = solved;
        }

        public boolean isSolved() {
            return isSolved;
        }

    }

    public void startPuzzleForCombat(Actor monster, PairQA randomPuzzle) { // Checks the Topic for the Monster
        topic topicType = topic.valueOf(monster.getType());
        Scanner console = new Scanner(System.in);
        if (validateTopic(topicType)) {
            System.out.println(randomPuzzle.getQuestion());
            String input = console.nextLine();
            if (input.equalsIgnoreCase(randomPuzzle.getAnswer())){
                randomPuzzle.setSolved(true);
                System.out.println("Correct!");
            } else {
                randomPuzzle.setSolved(false);
                System.out.println("Wrong!");
            }
        }
    }
    public void startPuzzleForRoom(Room room, PairQA randomPuzzle) { // Checks the Topic for the Monster
        topic topicType = topic.valueOf(room.getTopicType());
        Scanner console = new Scanner(System.in);
        if (validateTopic(topicType)) {
            System.out.println(randomPuzzle.getQuestion());
            String input = console.nextLine();
            if (input.equalsIgnoreCase(randomPuzzle.getAnswer())){
                randomPuzzle.setSolved(true);
            } else {
                randomPuzzle.setSolved(false);
            }
        }
    }


    public boolean validateTopic(topic inputTopic) {
        for (topic validTopic : topic.values()) {
            if (validTopic == inputTopic) {
                return true;
            }
        }
        return false;
    }


}

