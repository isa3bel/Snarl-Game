package model;

/**
 * An exit from a room to another room.
 */
public class Door extends Space {

  Door(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitDoor(this);
  }
}
