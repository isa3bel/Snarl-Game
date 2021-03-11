package testHarness.query;

import model.level.*;

/**
 * Visits a space to see if it is traversable (based on the LocationQuery).
 */
public class IsTraversable implements SpaceVisitor<Boolean> {

  /**
   * A door is traversable.
   * @param door the door
   * @return true
   */
  @Override
  public Boolean visitDoor(Door door) {
    return true;
  }

  /**
   * An exit is traversable.
   * @param exit the exit
   * @return true
   */
  @Override
  public Boolean visitExit(Exit exit) {
    return true;
  }

  /**
   * A wall is not in a room.
   * @param wall the wall
   * @return false
   */
  @Override
  public Boolean visitAnyWall(Wall wall) {
    return false;
  }

  /**
   * A tile is in this room if the group is not specified or the groups are the same.
   * @param tile the tile
   * @return true
   */
  @Override
  public Boolean visitTile(Tile tile) {
    return true;
  }

  /**
   * A HallwayTile is traversable.
   * @param tile the tile
   * @return true
   */
  @Override
  public Boolean visitHallwayTile(HallwayTile tile) {
    return true;
  }
}
