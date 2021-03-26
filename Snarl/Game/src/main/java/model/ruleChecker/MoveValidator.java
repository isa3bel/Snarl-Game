package model.ruleChecker;

import model.characters.Character;
import model.characters.Player;
import model.level.Level;
import model.level.Location;

import java.util.List;

/**
 * Validates the move of a character.
 */
public abstract class MoveValidator {

  protected Character character;
  protected Location nextMove;

  public MoveValidator(Character character, Location location) {
    this.character = character;
    this.nextMove = location;
  }

  /**
   * Moves the character to the location specified by nextMove.
   */
  public void executeMove() {
    this.character.moveTo(this.nextMove);
  }

  /**
   * Is the nextMove valid for this character?
   * @return if the move is valid
   */
  public abstract boolean isValid(Level level, List<Player> players);

}
