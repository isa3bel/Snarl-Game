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

  private static final int MOVE_DISTANCE = 2;

  public PlayerMoveValidator(Player character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Location currentLocation, Level level, List<Player> players) {
    boolean tileIsTraversable = this.isTraversable(level, new IsTraversable());
    boolean noPlayersOnSpace = players.stream()
            .filter(c -> !c.equals(this.character))
            .noneMatch(c -> this.nextMove.equals(c.getCurrentLocation()));
    boolean isWithin2Squares = this.isValidDistance(currentLocation, MOVE_DISTANCE);
    return tileIsTraversable && noPlayersOnSpace && isWithin2Squares;
  }
}
