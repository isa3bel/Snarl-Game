package model.level;

/**
 * Visits a space to see if it is in the same room as this Space group.
 */
public class IsValidStartingLocation implements SpaceVisitor<Boolean> {

  IsValidStartingLocation() {}

  @Override
  public Boolean visitDoor(Door door) {
    return false;
  }

  @Override
  public Boolean visitExit(Exit exit) {
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
