package model;

/**
 * An exit from the level.
 */
public class Exit extends Space {

  Exit(String room) {
    this.group = room;
  }

  public String acceptVisitor(SpaceVisitor visitor) {
    return visitor.visitExit(this);
  }
}
