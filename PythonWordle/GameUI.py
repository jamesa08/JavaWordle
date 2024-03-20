#!/usr/bin/env python3

# ----------------------------------------------------------------------
# GameUI.py
# James Alt
# 12/11/2022
# ----------------------------------------------------------------------

from __future__ import annotations

from typing import Dict, Optional, Union
from argparse import ArgumentParser

from graphics import *
from WordleGame import *
import random

class Tile:
    # the 1 letter string, must be exactly 1 character
    _letter: str
    # status of the tile. default is Guess.UNKNOWN
    _status: str

    # all the valid statuses of a guess
    VALID_STATUSES = {Guess.UNKNOWN, Guess.CORRECT, Guess.MISPLACED, Guess.NOT_USED}

    def __init__(self, letter: str, status: str = Guess.UNKNOWN):
        """
        dataclass for a tile
        :param letter: the 1 letter string, must be exactly 1 character
        :param status: status of the tile. default is Guess.UNKNOWN
        """
        assert len(letter) == 1
        self._letter = letter

        assert status in Tile.VALID_STATUSES
        self._status = status

    def setStatus(self, status: str):
        """
        sets the status of the tile
        :param status: the status to set, can be Guess.UNKNOWN, Guess.CORRECT, Guess.MISPLACED, Guess.NOT_USED
        :return: None
        """
        assert status in Tile.VALID_STATUSES
        self._status = status

    def getStatus(self) -> str:
        """
        gets the status of the current tile
        :return: str of {Guess.UNKNOWN, Guess.CORRECT, Guess.MISPLACED, Guess.NOT_USED}
        """
        return self._status

class TileUI(Tile):
    # the graphics window object
    _window: GraphWin
    # the center point at which you draw the tile
    _centerPt: Point
    # the rectangle object that holds the square
    _tile: Rectangle
    # the letter object that holds the actual letter
    _letter: Text

    # ideally this would be updated in the Guess class
    colorDict = {
        Guess.CORRECT: "green",
        Guess.MISPLACED: "yellow",
        Guess.NOT_USED: "grey",
        Guess.UNKNOWN: "white"
    }

    def __init__(self, letter: str, window: GraphWin, centerPt: Point, status: str = Guess.UNKNOWN, draw=True):
        """
        tile UI to display
        :param letter: the 1 letter string to draw, must be exactly 1 character
        :param window: the graphics window object
        :param centerPt: the center point at which to draw the tile
        :param status: status of the tile, which changes the background color. default is Guess.UNKNOWN
        :param draw: draw the tile right away on initialization. Default is True.
        """
        super().__init__(letter, status)
        self._window = window
        self._centerPt = centerPt
        size = GameUI.TILE_SIZE

        # calculate edges of the rectangle
        pX = Point(centerPt.getX() - size/2, centerPt.getY() - size/2)
        pY = Point(centerPt.getX() + size/2, centerPt.getY() + size/2)

        # create rectangle
        self._tile = Rectangle(pX, pY)
        self._tile.setFill(TileUI.colorDict[self.getStatus()])

        # create text object
        self._letter = Text(centerPt, self._letter)
        self._letter.setSize(max(5, min(size - 14, 36)))  # text between 5 and 36, minus 14 to add some padding

        if draw:
            self._tile.draw(window)
            self._letter.draw(window)

    def setStatus(self, status: str):
        """
        sets the stauts of the current tile
        :param status: status of {Guess.UNKNOWN, Guess.CORRECT, Guess.MISPLACED, Guess.NOT_USED}
        :return: None
        """
        super().setStatus(status)
        self._tile.setFill(TileUI.colorDict[self.getStatus()])

    def draw(self):
        """
        draws the current tile to the screen
        :return: None
        """
        self._tile.draw(self._window)
        self._letter.draw(self._window)

class KeyboardUI:
    """
    collection of computer keys displayed at a central point somewhere on the screen
    """
    TOP_ROW = "qwertyuiop"
    MIDDLE_ROW = "asdfghjkl"
    BOTTOM_ROW = "zxcvbnm"

    # the dictionary where all the TileUI objects are stored
    # "A" -> TileUI("A", ...)
    _keyDict: Dict[str, TileUI]

    def __init__(self, window: GraphWin, centerY: float):
        """
        create a keyboard
        :param window: window object
        :param centerY: the center y of the keyboard
        """
        self._keyDict = dict()

        # create keyboard
        # get the length of the entire keyboard (top row is the longest row, so this is the length)
        length = len(KeyboardUI.TOP_ROW) * GameUI.TILE_SIZE

        # this is how much to shift the keyboard by to get it into the center of the screen
        shift = (window.getWidth()/2 - (length/2)) + GameUI.TILE_SIZE/2

        # iterate over each row to draw each tile accordingly
        for i, letter in enumerate(KeyboardUI.TOP_ROW):
            tile = TileUI(window=window, letter=letter.upper(), centerPt=Point((i * GameUI.TILE_SIZE) + shift, centerY - GameUI.KEY_ROW_SP))
            self._keyDict[letter] = tile

        for i, letter in enumerate(KeyboardUI.MIDDLE_ROW):
            tile = TileUI(window=window, letter=letter.upper(), centerPt=Point((i * GameUI.TILE_SIZE) + shift + GameUI.KEY_MID_SHIFT, centerY))
            self._keyDict[letter] = tile

        for i, letter in enumerate(KeyboardUI.BOTTOM_ROW):
            tile = TileUI(window=window, letter=letter.upper(), centerPt=Point((i * GameUI.TILE_SIZE) + shift + GameUI.KEY_BOT_SHIFT, centerY + GameUI.KEY_ROW_SP))
            self._keyDict[letter] = tile

    def update(self, correctLetters: Set[str], misplacedLetters: Set[str], unusedLetters: Set[str]):
        """
        updates each tile on the keyboard with the appropriate letters
        :param correctLetters: the correct letters in the game, in the correct place
        :param misplacedLetters: the misplaced letters in the game, but still in the answers
        :param unusedLetters: the unused letters in the game, which are not used at all
        :return: None
        """
        # update the keyboard in order of not used, misplaced and then correct
        for letters, status in [(unusedLetters, Guess.NOT_USED), (misplacedLetters, Guess.MISPLACED), (correctLetters, Guess.CORRECT)]:
            for letter in letters:
                tile = self._keyDict[letter]
                tile.setStatus(status)

class GuessUI(Guess):
    """
    a 5-letter guess displayed at a central point somewhere on the screen
    """
    # the graphics window object
    _window: GraphWin
    # the guess you are on, number of guesses you've made so far
    _guessNum: int
    # the tiles of all the guess objects
    _guessTiles: List[TileUI]

    def __init__(self, window: GraphWin, guessNum: int, guess: str, answer: str, centerX: Optional[float] = None, centerY: float = 0.0, draw=True):
        """
        Make a Guess and draw it on screen
        :param window: Graphics Window object
        :param guessNum: the guess you are on, number of guesses you've made so far
        :param guess: the 5 letter guess
        :param answer: the correct 5 letter answer
        :param centerX: center X coordinate of the guess, in px
        :param centerY: center Y coordinate of the guess, in px
        :param draw: draw to the window object. default is True
        """
        super().__init__(guess, answer)
        self._window = window
        self._guessNum = guessNum
        self._guessTiles = []
        self._centerX = centerX
        self._centerY = centerY
        self._guess = guess
        self._guessNum = guessNum

        # at this point the guess has already been calculated,
        # and we can draw the guess tiles
        if draw:
            self.draw()

    def draw(self):
        """
        draw the tiles (if not already drawn)
        :return: None
        """
        pY = (GameUI.PADDING_TOP + (self._guessNum * GameUI.SP_BTWN_GUESS)) + self._centerY
        if not self._centerX:
            # put it in the center if there is no X value passed
            self._centerX = self._window.getWidth() / 2

        for i, char in enumerate(list(self._guess)):
            # width is GameUI.TILE_SIZE
            # to shift it to the center of the screen, we need to calculate the center of the screen
            # (or use provided calc) and subtract 2 tiles over from that
            pX = (i * GameUI.TILE_SIZE) + (self._centerX - (GameUI.TILE_SIZE * 2))
            tile = TileUI(letter=char.upper(), window=self._window, centerPt=Point(pX, pY), status=self.result()[i])
            self._guessTiles.append(tile)

class WordleGameUI(WordleGame):
    # the graphics window object
    _window: GraphWin
    # 5 letter answer
    _answer: str
    # KeyboardUI class
    _keyboard: KeyboardUI

    def __init__(self, window: GraphWin, answer: str, keyboard: KeyboardUI):
        """
        class for tracking multiple guesses and keeping and also displaying
        :param window: graphics window
        :param answer: the answer to the game
        :param keyboard: the KeyboardUI class to be updated
        """
        super().__init__(answer)
        self._window = window
        self._keyboard = keyboard

    def addGuess(self, guess: str) -> Guess:
        """
        process this guess updating the sets for the status of each letter
        will display the guess to the screen
        and will update the keyboard
        :param guess: 5 letter string corresponding to the guess
        :return: the Guess with information about the status of each letter
        throws a ValueError if the guess is not valid
        """
        guess = GuessUI(self._window, len(self._guesses), guess, self._answer, draw=False)

        # add to list and draw to screen
        self._guesses.append(guess)
        guess.draw()

        # update the correct letters with the correct ones from this guess
        self.correctLetters.update(guess.correctLetters)
        # update the misplaced letters with the misplaced ones from this guess
        self.misplacedLetters.update(guess.misplacedLetters)
        # update the unused letters with the unused ones from this guess
        self.unusedLetters.update(guess.unusedLetters)

        # need to remove any correct ones from the misplaced for correct color in keyboard
        self.misplacedLetters.difference_update(self.correctLetters)

        # update the keyboard
        self._keyboard.update(self.correctLetters, self.misplacedLetters, self.unusedLetters)

        return guess

    def numOfGuesses(self) -> int:
        """
        the number of guesses made
        :return: int, the number of guesses made
        """
        return len(self._guesses)

class GameUI:
    # the graphics window object
    _window: GraphWin
    # Keyboard class
    _keyboard: KeyboardUI
    # Set of valid guesses passed in via a file
    _validGuesses: Set[str]
    # The randomly chosen answer
    _answer: str

    # ///////// UI SETTINGS /////////
    # Settings are all in pixel units
    # Game UI settings
    SCALE_FACTOR = 1.3  # not in pixels

    PADDING_TOP = int(28 * SCALE_FACTOR)
    PADDING_BOTTOM = int(28 * SCALE_FACTOR)
    WIDTH = 500
    HEIGHT = 400

    # Tile UI settings
    TILE_SIZE = int(30 * SCALE_FACTOR)

    # Guess UI settings
    # spacing between each guess, in px
    SP_BTWN_GUESS = TILE_SIZE  # this will have 0 space between the guesses

    # Keyboard UI settings
    KEY_ROW_SP = TILE_SIZE  # row spacing based on tile height
    KEY_MID_SHIFT = TILE_SIZE / 2  # shift 1 half tile
    KEY_BOT_SHIFT = KEY_MID_SHIFT + TILE_SIZE  # shift 1 whole tile + MIDDLE_ROW_SHIFT

    def __init__(self, wordFilePath: str, answerFilePath: Optional[str] = None):
        """
        initializes game UI and chooses an answer from one of the file parameters
        :param wordFilePath: path to file with valid words
        :param answerFilePath: path file with possible answers to puzzle (if None, uses wordFile for possible answers)
        """

        self._window = GraphWin("Wordle", GameUI.WIDTH, GameUI.HEIGHT)

        # MacOS dark mode fix
        self._window.setBackground("white")

        self._keyboard = KeyboardUI(self._window, self._window.getHeight() - GameUI.PADDING_BOTTOM*2)

        # pick guesses from guess list
        with open(wordFilePath, 'r') as wordFile:
            self._validGuesses = set(wordFile.read().split("\n"))

        # pick answer from answers list (if provided)
        if answerFilePath:
            with open(answerFilePath, 'r') as answerFile:
                answers = answerFile.read().split("\n")
                self._answer = random.choice(answers)
        else:
            # pick answer from guess list if there is no answer file
            self._answer = random.sample(self._validGuesses, 1)[0]  # random.sample returns a list which is why [0]

        # create WordleGameUI
        self._wordleGameUI = WordleGameUI(self._window, self._answer, self._keyboard)

    def play(self, saveImage: bool = False):
        """
        plays the game, using commandline input and updates the UI
        :param saveImage: True if a screenshot is to be saved after each guess
        :return: None
        """
        correct = False

        while self._wordleGameUI.numOfGuesses() < 6 and not correct:
            # make certain user enters 5-letter word
            while True:
                guessStr = input("enter guess: ")
                if guessStr in self._validGuesses:
                    break
                else:
                    print(f"{guessStr} is not a valid word")

            guess = self._wordleGameUI.addGuess(guessStr)

            if saveImage:
                self._window.savePostscript(f"guess{self._wordleGameUI.numOfGuesses()}.ps")

            if guess.isCorrect():
                correct = True
                break

        if correct:
            print(f"\ncorrect in {self._wordleGameUI.numOfGuesses()}")
        else:
            print(f"\nthe answer is: {self._answer}")

        print("\npress <return>")
        input()

# ----------------------------------------------------------------------

def main():
    parser = ArgumentParser(description="plays one Wordle game with graphics")
    parser.add_argument("-s", "--screenshot", dest="screenshot", default=False, action="store_true",
                        help="take a screenshot after each turn")
    parser.add_argument("validWordFile",
                        help="path to valid word file")
    parser.add_argument('answerFile', nargs="?", default=None,
                        help="optional file for answers; uses validWordFile if none provided")
    options = parser.parse_args()

    # create UI and play the game
    gui = GameUI(options.validWordFile, options.answerFile)
    gui.play(options.screenshot)


# ----------------------------------------------------------------------

if __name__ == '__main__':
    main()