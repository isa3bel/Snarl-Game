package model.ruleChecker;

import java.util.List;
import model.characters.Player;
import model.characters.Zombie;
import model.level.*;

/**
 * Rule checker for the next move of a zombie.
 */
public class ZombieMoveValidator extends MoveValidator {

  public ZombieMoveValidator(Zombie zombie, Location location) {
    super(zombie, location);
  }

  @Override
  public boolean isValid(Level level, List<Player> players) {
    if (this.nextMove.equals(this.character.getCurrentLocation())) {
      return level.filter((space, location) -> location.isAdjacentTo(this.nextMove)
          && !this.isOccupiedByAdversary(level, location)
          && space.acceptVisitor(new ZombieCanWalkOnSpace())).isEmpty();
    }

    boolean isWithinOneSquare = this.isValidDistance(1);
    boolean canWalkOnSpace = this.isTraversable(level, new ZombieCanWalkOnSpace());
    boolean isOccupied = this.isOccupiedByAdversary(level, this.nextMove);
    return isWithinOneSquare && canWalkOnSpace && !isOccupied;
  }

  /**
   * Can a zombie walk on the given space?
   */
  private static class ZombieCanWalkOnSpace implements SpaceVisitor<Boolean> {

    @Override
    public Boolean visitDoor(Door door) {
      return false;
    }

    @Override
    public Boolean visitAnyWall(Wall wall) {
      return false;
    }

    @Override
    public Boolean visitTile(Tile tile) {
      return true;
    }

    @Override
    public Boolean visitHallwayTile(HallwayTile tile) {
      return false;
    }
  }
}
