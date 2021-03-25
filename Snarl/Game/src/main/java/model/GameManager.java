package model;

import java.util.ArrayList;

import model.characters.Character;
import model.ruleChecker.GameStateValidator;
import model.ruleChecker.GameStatus;
import model.ruleChecker.MoveValidator;
import model.ruleChecker.Interaction;
import model.level.Level;
import view.View;

/**
 * The GameManager for the Snarl game.
 */
public class GameManager {

  private int currentLevel;
  private final Level[] levels;
  private final ArrayList<Character> characters;
  private final GameStateValidator gameStateValidator;
  private final Publisher publisher;

  public GameManager(int currentLevel, Level[] levels, ArrayList<Character> characters) {
    this.currentLevel = currentLevel;
    this.levels = levels;
    this.characters = characters;
    this.gameStateValidator = new GameStateValidator();
    this.publisher = new Publisher();
  }

  public void addObserver(Observer o) {
    this.publisher.addObserver(o);
  }

  /**
   * Advances the game through a round, which is a turn for every character in the game.
   * @return the status of the game at the end of the round
   */
  public GameStatus doRound() {
    this.characters.forEach(this::doTurn);
    return this.gameStateValidator.evaluateGameState(this.currentLevel, this.levels, this.characters);
  }

  /**
   * Does this character's turn in the game.
   * @param currentCharacter the character whose turn it is
   */
  public void doTurn(Character currentCharacter) {
    currentCharacter.updateController(this);
    MoveValidator moveValidator;

    do {
      moveValidator = currentCharacter.getNextMove();
      // TODO: number of retries?
    } while (!this.isMoveValid(moveValidator));
    moveValidator.executeMove();

    Interaction interaction = currentCharacter.makeInteraction();
    this.characters.forEach(character -> character.acceptVisitor(interaction));
    this.levels[this.currentLevel].interact(currentCharacter.getCurrentLocation(), interaction);

    this.publisher.update(this);
  }

  /**
   * Adds the contents of this builder to the view.
   * @param view the view to view this game through
   */
  public void buildView(View view) {
    view.renderLevel(this.levels[this.currentLevel]);
    view.placeCharacters(this.characters);
  }

  /**
   * Are the rules of the given moveValidator valid for this game state?
   * @param moveValidator the move validator to validate the move
   * @return whether this game state is compatible with the given move validator
   */
  public boolean isMoveValid(MoveValidator moveValidator) {
    return moveValidator.isValid(this.levels[this.currentLevel], this.characters);
  }

}
