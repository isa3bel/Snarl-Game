package model;

/**
 * An exit from the level.
 */
public class Exit extends Space {

  private boolean isLocked;

  /**
   * Creates an exit in the given room.
   * @param room the room that this exit will be in
   */
  Exit(String room) {
    this(room, false);
  }

  /**
   * Creates an exit in the given room with a locked status.
   * @param room the room this exit is in
   * @param locked the locked status of this exit
   */
  Exit(String room, boolean locked) {
    this.group = room;
    this.isLocked = locked;
  }

  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitExit(this);
  }

  public void unlock() {
    this.isLocked = false;
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    if (this.isLocked) return;
    visitor.visitExit(this);
  }
}
