package model;

import java.util.ArrayList;
import view.View;

/**
 * The GameManager for the Snarl game.
 */
public class GameManager {

  private int currentLevel;
  private final Level[] levels;
  private final ArrayList<Character> characters;

  GameManager(int currentLevel, Level[] levels, ArrayList<Character> characters) {
    this.currentLevel = currentLevel;
    this.levels = levels;
    this.characters = characters;
  }

  /**
   * Advance this game to the next level.
   */
  public void nextLevel() {
    if (++this.currentLevel == levels.length) {
      // game finished - do something?
      return;
    }

    ArrayList<Location> playerLocations = this.levels[this.currentLevel].calculatePlayerLocations();
    this.characters
        .stream()
        // TODO: bad - move adversary to level ?
        .filter(character -> character instanceof Player)
        .forEach(character -> character.moveTo(playerLocations.remove(0)));
  }

  /**
   * Advances the game through a round, which is a turn for every character in the game.
   */
  public void doRound() {
    this.characters.forEach(this::doTurn);
  }

  /**
   * Does this character's turn in the game.
   * @param currentCharacter the character whose turn it is
   */
  private void doTurn(Character currentCharacter) {
    MoveValidator moveValidator;
    do {
      moveValidator = currentCharacter.getNextMove();
    } while (!moveValidator.isValid(this.levels[this.currentLevel], this.characters));
    moveValidator.executeMove();

    Interaction interaction = currentCharacter.makeInteraction(this);
    this.characters.forEach(character -> character.acceptVisitor(interaction));
    this.levels[this.currentLevel].interact(currentCharacter.getCurrentLocation(), interaction);
  }

  /**
   * Adds the contents of this builder to the view.
   * @param view the view to view this game through
   */
  public void buildView(View view) {
    view.renderLevel(this.levels[this.currentLevel]);
    view.placeCharacters(this.characters);
  }

}
