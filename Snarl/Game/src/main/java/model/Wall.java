package model;

/**
 * A space that is not available for characters or objects.
 */
public class Wall extends Space {

  Wall() {}

  Wall(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitWall(this);
  }
}
