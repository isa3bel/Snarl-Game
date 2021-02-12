package model;

/**
 * An exit from a room to another room.
 */
public class Door extends Space {

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitDoor(this);
  }
}
