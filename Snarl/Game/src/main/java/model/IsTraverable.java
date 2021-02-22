package model;

/**
 * Currently for the express purposes of the LocationQuery in the test harness, is a space is "traversable"?
 */
public class IsTraverable implements SpaceVisitor<Boolean> {

  /**
   * A door is not walkable.
   * @param door the door
   * @return false
   */
  @Override
  public Boolean visitDoor(Door door) {
    return false;
  }

  /**
   * An exit is not walkable.
   * @param exit the exit
   * @return false
   */
  @Override
  public Boolean visitExit(Exit exit) {
    return false;
  }

  /**
   * A wall is not walkable.
   * @param wall the wall
   * @return false
   */
  @Override
  public Boolean visitWall(Wall wall) {
    return false;
  }

  /**
   * A tile is walkable.
   * @param tile the tile
   * @return true
   */
  @Override
  public Boolean visitTile(Tile tile) {
    return true;
  }
}
