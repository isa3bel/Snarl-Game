package model;

/**
 * A key for a level exit in the Snarl game.
 */
public class Key extends Item {

  Key(Location loc) {
    super(loc);
  }

  public <T> T acceptVisitor(ItemVisitor<T> visitor) {
    return visitor.visitKey(this);
  }
}
