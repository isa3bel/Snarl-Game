package model;

/**
 * An exit from a room to another room.
 */
public class Door extends Space {

  /**
   * Constructor for this Door
   * @param room the room that this door will be in
   */
  Door(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitDoor(this);
  }

}
