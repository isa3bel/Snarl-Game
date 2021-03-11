package model.item;

import model.characters.Player;
import model.level.Exit;
import model.level.Location;

/**
 * A key for a level exit in the Snarl game.
 */
public class Key extends Item {

  private final Exit exit;

  public Key(Location loc, Exit exit) {
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
