package model.ruleChecker;

import model.characters.Player;
import model.level.Level;
import model.level.Location;
import testHarness.query.IsTraversable;

import java.util.List;

/**
 * Validates the move of a player.
 */
public class PlayerMoveValidator extends MoveValidator {

  public PlayerMoveValidator(Player character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, List<Player> players) {
    boolean tileIsTraversable = level.get(this.nextMove).acceptVisitor(new IsTraversable());
    boolean noPlayersOnSpace = players.stream()
            .filter(c -> !c.equals(this.character))
            .noneMatch(c -> this.nextMove.equals(c.getCurrentLocation()));
    boolean isWithin2Squares = this.nextMove.euclidianDistance(this.character.getCurrentLocation()) <= 2;

    return tileIsTraversable && noPlayersOnSpace && isWithin2Squares;
  }

}
