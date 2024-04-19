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
    List<String> _result;
    // whether the guess is completely correct (true if it is)
    boolean _isCorrect;
    // set containing the correct letters for the guess
    Set<String> correctLetters;
    // set containing the misplaced letters for the guess
    Set<String> misplacedLetters;
    // set containing unusedLetters
    Set<String> unusedLetters;


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
        correctLetters = new HashSet<>();
        misplacedLetters = new HashSet<>();
        unusedLetters = new HashSet<>();
        _isCorrect = _evaluate(answer);
    }

    public String guess() {

    }

    public String result() {

    }

    private void _evaluate(String answer) {

    }

    public boolean isCorrect() { return _isCorrect;}
}
