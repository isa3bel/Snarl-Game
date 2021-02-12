package model;

/**
 * An exit from a room to another room.
 */
public class Door extends Space {

  Door(String room) {
    this.group = room;
  }

  @Override
  public String acceptVisitor(SpaceVisitor visitor) {
    return visitor.visitDoor(this);
  }
}
