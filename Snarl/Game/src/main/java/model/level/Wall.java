package model.level;

/**
 * A space that is not available for characters or objects.
 */
public class Wall extends Space {

  protected Wall() {}

  /**
   * Constructor for this Wall
   * @param room the room that this wall will be in
   */
  public Wall(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitAnyWall(this);
  }

  public <T> T acceptWallVisitor(WallVisitor<T> visitor) {
    return visitor.visitWall(this);
  }
}
