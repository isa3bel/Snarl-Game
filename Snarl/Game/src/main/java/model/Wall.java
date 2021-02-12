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
  public String acceptVisitor(SpaceVisitor visitor) {
    return visitor.visitWall(this);
  }
}
