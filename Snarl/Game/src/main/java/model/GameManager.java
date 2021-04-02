package model;

import java.util.ArrayList;

import java.util.Collections;
import model.observer.Observer;
import model.observer.Publisher;
import model.characters.Character;
import model.characters.Player;
import model.level.Location;
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
   * Adds an observer to the publisher field
   * @param observer the observer to be added
   */
  public void addObserver(Observer observer) {
    this.publisher.addObserver(observer);
  }

  /**
   * Advances the game through a round, which is a turn for every character in the game.
   * @return the status of the game at the end of the round
   */
  public GameStatus doRound() {
    for(Player player : this.players) {
      GameStatus status = this.doTurn(player);
      if (status != GameStatus.PLAYING) return status;
    }
    return this.levels[this.currentLevel].doAdversaryTurns(this);
  }

  /**
   * If the players have exited, advance the level and reset their locations.
   */
  private void maybeAdvanceLevel() {
    if (!this.gameStateValidator.shouldAdvanceLevel(this.players)) return;

    if (++this.currentLevel == this.levels.length) return;

    ArrayList<Location> validPlayerStartingLocations = this.levels[this.currentLevel].calculateValidActorPositions();

    if (validPlayerStartingLocations.size() < this.players.size()) {
      throw new IllegalStateException("level does not have enough valid starting positions for players");
    }

    this.players.forEach(player -> player.moveTo(validPlayerStartingLocations.remove(0)));
    this.updatePlayers();
  }

  /**
   * Determines the current game status and potentially updates the game or players accordingly.
   * @return the status of the game
   */
  private GameStatus checkGameStatus() {
    GameStatus status = this.gameStateValidator.evaluateGameState(this.currentLevel, this.levels, this.players);
    switch (status) {
      case LOST:
        System.out.println("You lost at level " + this.currentLevel);
        break;
      case WON:
        System.out.println("You won!");
        Collections.sort(this.players);
        this.players.forEach(player -> System.out.println(player.leaderBoard()));
        break;
    }

    return status;
  }

  /**
   * Updates all the players in this game with the game state.
   */
  public void updatePlayers() {
    this.players.forEach(character -> character.updateController(this));
  }

  /**
   * Does this character's turn in the game.
   * @param currentCharacter the character whose turn it is
   */
  public GameStatus doTurn(Character currentCharacter) {
    if (!currentCharacter.isInGame()) return GameStatus.PLAYING;

    MoveValidator moveValidator;
    do {
      moveValidator = currentCharacter.getNextMove();
    } while (!this.isMoveValid(moveValidator));
    moveValidator.executeMove();

    Interaction interaction = currentCharacter.makeInteraction();
    this.levels[this.currentLevel].interact(interaction);
    this.players.forEach(player -> {
      if (player.isInGame()) player.updateController(this);
    });
    this.publisher.update(this);

    this.maybeAdvanceLevel();
    return checkGameStatus();
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
