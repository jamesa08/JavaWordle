#!/usr/bin/env python3

# ----------------------------------------------------------------------
# WordleGame.py
# Dave Reed
# 06/30/2022
# ----------------------------------------------------------------------

from __future__ import annotations
from typing import List, Set


class Guess:
    """
    class for computing which letters are correct, which are misplaced, and which are not part of the answer
    for a 5 letter Wordle guess
    """
    # initial state of a letter until we determine its correct state
    UNKNOWN = "U"
    # letter is in correct position
    CORRECT = "C"
    # letter is used at a location not already marked CORRECT
    MISPLACED = "M"
    # letter is not used at all
    NOT_USED = "I"

    # background color to use when displaying a letter based on its status
    colorDict = {
        CORRECT: "green",
        MISPLACED: "yellow",
        NOT_USED: "grey"
    }

    # the guess
    _guess: str
    # list corresponding to status of each of the five letters in the guess
    # (one of Guess.UNKNOWN, Guess.CORRECT, Guess.MISPLACED, Guess.NOT_USED)
    _result: List[str]
    # whether the guess is completely correct (True if it is)
    _isCorrect: bool
    # set containing the correct letters for the guess (this should be read-only outside the class)
    correctLetters: Set[str]
    # set containing the misplaced letters for the guess (this should be read-only outside the class)
    misplacedLetters: Set[str]
    # set containing the unused letters for the guess (this should be read-only outside the class)
    unusedLetters: Set[str]

    def __init__(self, guess: str, answer: str):
        """
        :param guess: the 5 letter guess
        :param answer: the correct 5 letter answer
        """
        assert len(guess) == 5 and len(answer) == 5
        # save the guess for later processing and returning
        self._guess = guess
        # initial state of each letter
        self._result = 5 * [Guess.UNKNOWN]
        self.correctLetters = set()
        self.misplacedLetters = set()
        self.unusedLetters = set()
        self._isCorrect = self._evaluate(answer)

    def guess(self) -> str:
        """
        :return: the 5 letter guess that was evaluated
        """
        return self._guess

    def result(self) -> List[str]:
        """
        :return: list corresponding to status of each of the five letters in the guess; each
        entry in list is one of Guess.UNKNOWN, Guess.CORRECT, Guess.MISPLACED, Guess.NOT_USED
        """
        return self._result

    def _evaluate(self, answer: str):
        """
        helper method to evaluate the guess
        :param answer: the answer to the puzzle for evaluating the guess
        :return: True if guess is completely correct, False otherwise
        """
        answerList = list(answer)
        # iterate in reverse order since deleting from answer
        for i in reversed(range(5)):
            # letter is in correct spot
            if self._guess[i] == answerList[i]:
                # mark as correct
                self._result[i] = Guess.CORRECT
                # put it in correct set
                self.correctLetters.add(self._guess[i])
                # remove so this one isn't later used to mark same letter MISPLACED
                del answerList[i]
            # letter is not used
            elif self._guess[i] not in answerList:
                # letter not in answer
                self.unusedLetters.add(self._guess[i])
                self._result[i] = Guess.NOT_USED

        # # initially mark as CORRECT or NOT_USED
        # for i, ch in enumerate(self._guess):
        #     # letter is in correct spot
        #     if ch == answer[i]:
        #         # mark as correct
        #         self._result[i] = Guess.CORRECT
        #         self.correctLetters.add(ch)
        #     # letter is not used
        #     elif self._guess[i] not in answer:
        #         # letter not in answer
        #         self._result[i] = Guess.NOT_USED
        #         self.unusedLetters.add(ch)

        # make answerList with only letters from answer that are not correct so we don't count correct
        # letters as misplaced
        answerList = []
        for (i, status) in enumerate(self._result):
            if status != Guess.CORRECT:
                answerList.append(answer[i])

        # any letters still UNKNOWN are either MISPLACED or NOT_USED
        # can't just mark as MISPLACED in previous loop since could be a duplicate of a correct letter
        # with the letter already in the correct position
        # process and check against updated answer that has correct letters removed
        for (i, status) in enumerate(self._result):
            # statuses that are still UNKNOWN may be MISPLACED or NOT_USED
            if status == Guess.UNKNOWN:
                # get the letter
                ch = self._guess[i]
                # if still in answer after removed CORRECT ones, this is misplaced
                if ch in answerList:
                    self._result[i] = Guess.MISPLACED
                    # remove one copy so can't use this one again if guess contains repeated letter
                    answerList.remove(ch)
                    # if it's not a correct letter, add to misplaced
                    if ch not in self.correctLetters:
                        self.misplacedLetters.add(ch)
                else:
                    # if still not in answer, this copy of letter is NOT_USED
                    self._result[i] = Guess.NOT_USED

        # determine if guess is correct
        for r in self._result:
            # if one letter is not CORRECT, it is wrong
            if r != Guess.CORRECT:
                return False
        # all guesses were correct, so the answer was found
        return True

    def isCorrect(self) -> bool:
        """
        :return: True, if the guess is the answer, False otherwise
        """
        return self._isCorrect

# ----------------------------------------------------------------------


class WordleGame:
    """
    class for tracking multiple guesses and keeping
    """

    # the correct answer to the game
    _answer: str
    # the guesses the user makes
    _guesses: List[Guess]

    # sets for getting correct colors for keyboard

    # letters that are in the answer (this should be read-only outside the class)
    correctLetters: Set[str]
    # letters that are not in the answer but are misplaced (this should be read-only outside the class)
    misplacedLetters: Set[str]
    # letters that are not in the answer (this should be read-only outside the class)
    unusedLetters: Set[str]

    # ------------------------------------------------------------------

    def __init__(self, answer: str):
        """
        :param answer: the correct answer to the puzzle
        """
        self._answer = answer
        self._guesses = []
        self.correctLetters = set()
        self.misplacedLetters = set()
        self.unusedLetters = set()

    def addGuess(self, guess: str) -> Guess:
        """
        process this guess updating the sets for the status of each letter
        :param guess: 5 letter string corresponding to the guess
        :return: the Guess with information about the status of each letter
        """
        guess = Guess(guess, self._answer)
        self._guesses.append(guess)
        # update the correct letters with the correct ones from this guess
        self.correctLetters.update(guess.correctLetters)
        # update the misplaced letters with the misplaced ones from this guess
        self.misplacedLetters.update(guess.misplacedLetters)
        # update the unused letters with the unused ones from this guess
        self.unusedLetters.update(guess.unusedLetters)

        # need to remove any correct ones from the misplaced for correct color in keyboard
        self.misplacedLetters.difference_update(self.correctLetters)
        return guess
