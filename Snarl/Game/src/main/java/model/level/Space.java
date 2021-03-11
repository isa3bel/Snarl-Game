package model.level;

import model.interactions.Interactable;
import model.interactions.InteractableVisitor;

/**
 * A space in the 2d plane of the dungeon crawler.
 */
public abstract class Space implements Interactable {

  // the room or hallway identifier of this space
  protected String group;

  public abstract <T> T acceptVisitor(SpaceVisitor<T> visitor);

  public boolean sameGroup(Space that) {
    return this.group != null && this.group.equals(that.group);
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    // do nothing most of the time
  }
}
