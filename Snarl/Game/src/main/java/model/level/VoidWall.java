package model.level;

/**
 * A space that is not available for characters or objects.
 */
public class VoidWall extends Wall {

  @Override
  public <T> T acceptWallVisitor(WallVisitor<T> visitor) {
    return visitor.visitVoidWall(this);
  }
}
