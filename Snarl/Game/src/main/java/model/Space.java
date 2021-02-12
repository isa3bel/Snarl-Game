package model;

/**
 * A space in the 2d plane of the dungeon crawler.
 */
public abstract class Space {

  // the room or hallway identifier of this space
  String group;

  public abstract String acceptVisitor(SpaceVisitor visitor);
}
