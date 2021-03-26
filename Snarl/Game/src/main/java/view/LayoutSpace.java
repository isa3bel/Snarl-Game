package view;

import model.level.*;

/**
 * The representation of a space according to the provided layout in the test harness.
 */
public class LayoutSpace implements SpaceVisitor<String> {

  @Override
  public String visitDoor(Door door) {
    return "2";
  }

  @Override
  public String visitExit(Exit exit) {
    // TODO: make exit have a composite space of the space it replaces
    return "1";
  }

  @Override
  public String visitAnyWall(Wall wall) {
    return "0";
  }

  @Override
  public String visitTile(Tile tile) {
    return "1";
  }

  @Override
  public String visitHallwayTile(HallwayTile tile) {
    return "1";
  }
}
