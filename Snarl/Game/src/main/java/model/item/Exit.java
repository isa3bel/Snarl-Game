package model.item;

import model.level.Location;
import model.ruleChecker.InteractableVisitor;

/**
 * An exit from the level.
 */
public class Exit extends Item {

  private boolean isLocked;

  /**
   * Creates an exit in the given room with a locked status.
   * @param isLocked the locked status of this exit
   */
  public Exit(Location location, boolean isLocked) {
    super(location);
    this.isLocked = isLocked;
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitExit(this);
  }

  public <T> T acceptVisitor(ItemVisitor<T> visitor) {
    return visitor.visitExit(this);
  }

  public void unlock() {
    this.isLocked = false;
  }

  public boolean isLocked() {
    return this.isLocked;
  }

}
