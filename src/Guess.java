import java.util.*;

public class Guess {
    // initial state of letter until we determine its correct position
    String  UNKNOWN = "U";
    // letter is in correct position
    String CORRECT = "C";
    // letter is used at a location not already marked CORRECT
    String MISPLACED = "M";
    // letter is not used at all
    String NOT_USED = "I";


    Hashtable<String, String> colorDict= new Hashtable<>();

    // instance variables
    // the guess
    String _guess;
    // list corresponding to status of each of the five letters in the guess
    List<String> _result = new ArrayList<>();
    // whether the guess is completely correct (true if it is)
    boolean _isCorrect;
    // set containing the correct letters for the guess
    Set<Character> correctLetters;
    // set containing the misplaced letters for the guess
    Set<Character> misplacedLetters;
    // set containing unusedLetters
    Set<Character> unusedLetters;


    public Guess(String guess, String answer) {
        colorDict.put("COLOR", "green");
        colorDict.put("MISPLACED", "yellow");
        colorDict.put("NOT_USED", "grey");

        assert guess.length() == 5 && answer.length() == 5;
        // save the guess for later processing and returning
        _guess = guess;
        // initial state of each letter
        for (int i = 0; i < 5; i++) {
            _result.add(UNKNOWN);
        }
        correctLetters = new LinkedHashSet<>();
        misplacedLetters = new LinkedHashSet<>();
        unusedLetters = new LinkedHashSet<>();
        _isCorrect = _evaluate(answer);
    }

    public String guess() { return _guess;}

    public List<String> result() { return _result;}

    // helper method to evaluate guess
    private boolean _evaluate(String answer) {
//        List<Character> answerList = new ArrayList<>();
//        for (int i = 0; i < answer.length(); i++) {
//            answerList.add(answer.charAt(i));
//        }
        char[] arrayGuess = _guess.toCharArray();
        char[] answerList = answer.toCharArray();

        // iterate in reverse order since deleting from answer
        for (int i = 5; i > 0; i--) {
            // if letter is in correct spot
            if (arrayGuess[i] == answerList[i]) {
                // mark as correct
                _result.set(i,CORRECT);
                // put it in correct set
                correctLetters.add(arrayGuess[i]);
                // remove so it isn't used later to mark same letter MISPLACED
                if (answerList.length != 0) {
                    String str = new String(answerList);
                    String newStr = str.substring(0, str.length() - 1);
                    answerList = newStr.toCharArray();
                }
            // letter is not used
            }
        }
        // TEMP, DELETE
        return false;
    }

    public boolean isCorrect() { return _isCorrect;}
}
