package model.ruleChecker;

import model.level.*;

/**
 * Is the given space a valid starting location?
 */
public class IsValidStartingLocation implements SpaceVisitor<Boolean> {

  public IsValidStartingLocation() {}

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
