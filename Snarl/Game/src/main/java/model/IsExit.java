package model;

/**
 * Currently for the express purposes of the LocationQuery in the test harness, is a space is "traversable"?
 */
public class IsExit implements SpaceVisitor<Boolean> {

  /**
   * A door is not an exit.
   * @param door the door
   * @return false
   */
  @Override
  public Boolean visitDoor(Door door) {
    return false;
  }

  /**
   * An exit is not an exit.
   * @param exit the exit
   * @return false
   */
  @Override
  public Boolean visitExit(Exit exit) {
    return true;
  }

  /**
   * A wall is not an exit.
   * @param wall the wall
   * @return false
   */
  @Override
  public Boolean visitWall(Wall wall) {
    return false;
  }

  /**
   * A tile is not an exit.
   * @param tile the tile
   * @return true
   */
  @Override
  public Boolean visitTile(Tile tile) {
    return false;
  }

  /**
   * A tile is not an exit.
   * @param tile the tile
   * @return true
   */
  @Override
  public Boolean visitHallwayTile(HallwayTile tile) {
    return false;
  }
}
