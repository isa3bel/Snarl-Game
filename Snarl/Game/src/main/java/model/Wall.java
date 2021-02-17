package model;

/**
 * A space that is not available for characters or objects.
 */
public class Wall extends Space {

  Wall() {}

  /**
   * Constructor for this Wall
   * @param room the room that this wall will be in
   */
  Wall(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitWall(this);
  }
}
