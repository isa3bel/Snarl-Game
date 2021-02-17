package model;

/**
 * An exit from the level.
 */
public class Exit extends Space {

  /**
   * Constructor for this Exit
   * @param room the room that this exit will be in
   */
  Exit(String room) {
    this.group = room;
  }

  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitExit(this);
  }
}
