package view;

import java.util.function.Function;

import model.level.*;
import model.level.Door;
import model.level.Exit;
import model.level.HallwayTile;

/**
 * Translates a space to its ASCII representation.
 */
class ASCIISpace implements SpaceVisitor<String>, Function<Space, String> {

  @Override
  public String visitDoor(Door door) {
    return "D";
  }

  @Override
  public String visitExit(Exit exit) {
    return "E";
  }

  @Override
  public String visitAnyWall(Wall wall) {
    return "X";
  }

  @Override
  public String visitTile(Tile tile) {
    return " ";
  }

  @Override
  public String visitHallwayTile(HallwayTile tile) {
    return this.visitTile(tile);
  }

  @Override
  public String apply(Space space) {
    return space.acceptVisitor(this);
  }
}