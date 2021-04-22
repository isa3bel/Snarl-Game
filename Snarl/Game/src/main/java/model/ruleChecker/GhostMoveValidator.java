package model.ruleChecker;

import java.util.List;
import model.characters.Ghost;
import model.characters.Player;
import model.level.*;

/**
 * Rule checker for the next move of a ghost.
 */
public class GhostMoveValidator extends MoveValidator {

  public GhostMoveValidator(Ghost ghost, Location location) {
    super(ghost, location);
  }

  @Override
  public boolean isValid(Location currentLocation, Level level, List<Player> players) {
    if (this.nextMove.equals(currentLocation)) {
      return level.filter((space, location) -> location.isAdjacentTo(this.nextMove)
          && !this.isOccupiedByAdversary(level, location)).isEmpty();
    }

    // DECISION: ghosts cannot occupy the same tiles as other adversaries
    boolean isWithinOneSquare = this.isValidDistance(currentLocation, 1);
    boolean isOccupied = this.isOccupiedByAdversary(level, this.nextMove);
    return isWithinOneSquare && !isOccupied;
  }
}
