package model.level;

import model.ruleChecker.InteractableVisitor;

/**
 * A space in the 2d plane of the dungeon crawler.
 */
public abstract class Space {

  // the room or hallway identifier of this space
  protected String group;

  public abstract <T> T acceptVisitor(SpaceVisitor<T> visitor);

  public boolean sameGroup(Space that) {
    return this.group != null && this.group.equals(that.group);
  }

  public <T> void acceptVisitor(InteractableVisitor<T> visitor) {
    // should do nothing except for wall
  }

}
