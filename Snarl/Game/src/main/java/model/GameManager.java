package model;

import java.util.ArrayList;

import model.characters.Character;
import model.characters.Player;
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
  private final ArrayList<Player> players;
  private final GameStateValidator gameStateValidator;
  private final Publisher publisher;

  public GameManager(int currentLevel, Level[] levels, ArrayList<Player> players) {
    this.currentLevel = currentLevel;
    this.levels = levels;
    this.players = players;
    this.gameStateValidator = new GameStateValidator();
    this.publisher = new Publisher();
  }

  /**
   * Adds an observer to the GameManager.
   * @param observer the observer to add
   */
  public void addObserver(Observer observer) {
    this.publisher.addObserver(observer);
  }

  /**
   * Advances the game through a round, which is a turn for every character in the game.
   * @return the status of the game at the end of the round
   */
  public GameStatus doRound() {
    this.players.forEach(this::doTurn);
    // this.levels[this.currentLevel].moveAdversaries();
    return this.gameStateValidator.evaluateGameState(this.currentLevel, this.levels, this.players);
  }

  /**
   * Updates all the players in this game - TODO: explicit method should only be for testing task of milestone 7.
   */
  public void updatePlayers() {
    this.players.forEach(character -> character.updateController(this));
  }

  /**
   * Does this character's turn in the game.
   * @param currentCharacter the character whose turn it is
   */
  public void doTurn(Character currentCharacter) {
    if (!currentCharacter.isInGame()) return;

    MoveValidator moveValidator;
    do {
      moveValidator = currentCharacter.getNextMove();
      // TODO: number of retries?
    } while (!this.isMoveValid(moveValidator));
    moveValidator.executeMove();

    Interaction interaction = currentCharacter.makeInteraction();
    this.levels[this.currentLevel].interact(currentCharacter.getCurrentLocation(), interaction);
    this.players.forEach(player -> player.updateController(this));

    this.publisher.update(this);
  }

  /**
   * Adds the contents of this builder to the view.
   * @param view the view to view this game through
   */
  public void buildView(View view) {
    view.renderLevel(this.levels[this.currentLevel]);
    view.placePlayers(this.players);
  }

  /**
   * Are the rules of the given moveValidator valid for this game state?
   * @param moveValidator the move validator to validate the move
   * @return whether this game state is compatible with the given move validator
   */
  public boolean isMoveValid(MoveValidator moveValidator) {
    return moveValidator.isValid(this.levels[this.currentLevel], this.players);
  }

}
