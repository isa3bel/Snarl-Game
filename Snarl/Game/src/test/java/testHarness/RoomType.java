package testHarness;

import model.*;

public class RoomType implements SpaceVisitor<String>, WallVisitor<String> {

  @Override
  public String visitDoor(Door door) {
    return "room";
  }

  @Override
  public String visitExit(Exit exit) {
    return "room";
  }

  @Override
  public String visitAnyWall(Wall wall) {
    return wall.acceptWallVisitor(this);
  }

  @Override
  public String visitWall(Wall wall) {
    return "room";
  }

  @Override
  public String visitEdgeWall(EdgeWall wall) {
    return this.visitAnyWall(wall);
  }

  @Override
  public String visitVoidWall(VoidWall wall) {
    return "void";
  }

  @Override
  public String visitTile(Tile tile) {
    return "room";
  }

  @Override
  public String visitHallwayTile(HallwayTile tile) {
    return "hallway";
  }
}
