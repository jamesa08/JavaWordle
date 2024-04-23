import java.util.*;


// class for tracking multiple guesses
public class WordleGame {
    // correct answer to the game
    private String _answer;
    // user's guess
    private List<Guess> _guesses;
    // sets for getting correct colors for keyboard
    public Set<Character> correctLetters;
    public Set<Character> misplacedLetters;
    public Set<Character> unusedLetters;

    public WordleGame(String answer) {
        _answer = answer;
        _guesses = new ArrayList<>();
        correctLetters = new HashSet<>();
        misplacedLetters = new HashSet<>();
        unusedLetters = new HashSet<>();
    }

    public Guess addGuess(String guess) {
        Guess newGuess = new Guess(guess, _answer);
        _guesses.add(newGuess);
        // update sets
        correctLetters.addAll(newGuess.correctLetters);
        misplacedLetters.addAll(newGuess.misplacedLetters);
        unusedLetters.addAll(newGuess.unusedLetters);
        // remove correct ones from misplaced
        misplacedLetters.removeAll(correctLetters);

        return newGuess;
    }
}
