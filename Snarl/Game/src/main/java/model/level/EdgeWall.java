package model.level;

import model.level.Wall;
import model.level.WallVisitor;

/**
 * A wall space on the outer boundary of a room.
 */
public class EdgeWall extends Wall {

  public EdgeWall(String group) {
    super(group);
  }

  @Override
  public <T> T acceptWallVisitor(WallVisitor<T> visitor) {
    return visitor.visitEdgeWall(this);
  }
}
