package model.level;

import model.ruleChecker.InteractableVisitor;

/**
 * An exit from the level.
 */
public class Exit extends Space {

  private boolean isLocked;

  /**
   * Creates an exit in the given room with a locked status.
   * @param replacingSpace the space that this exit is replacing
   * @param isLocked the locked status of this exit
   */
  public Exit(Space replacingSpace, boolean isLocked) {
    this.group = replacingSpace.group;
    this.isLocked = isLocked;
  }

  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitExit(this);
  }

  public void unlock() {
    this.isLocked = false;
  }

  public boolean isLocked() {
    return this.isLocked;
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitExit(this);
  }
}
