package model;

/**
 * An exit from the level.
 */
public class Exit extends Space {

  Exit(String room) {
    this.group = room;
  }

  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitExit(this);
  }
}
