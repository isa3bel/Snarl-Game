package model;

/**
 * A key for a level exit in the Snarl game.
 */
public class Key extends Item {

  private Exit exit;

  Key(Location loc, Exit exit) {
    super(loc);
    this.exit = exit;
  }

  public <T> T acceptVisitor(ItemVisitor<T> visitor) {
    return visitor.visitKey(this);
  }

  @Override
  public void pickedUp(Player player) {
    this.currentLocation = null;
    this.exit.unlock();
  }

}
