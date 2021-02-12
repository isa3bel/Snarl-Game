package model;

/**
 * A space that is not available for characters or objects.
 */
public class Wall extends Space {

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitWall(this);
  }
}
