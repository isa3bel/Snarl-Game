package model.item;

import model.characters.Player;
import model.level.Ejected;
import model.level.Location;
import model.ruleChecker.InteractableVisitor;

/**
 * A key for a level exit in the Snarl game.
 */
public class Key extends Item {

  private final Exit exit;

  public Key(Location loc, Exit exit) {
    super(loc);
    this.exit = exit;
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitKey(this);
  }

  public <T> T acceptVisitor(ItemVisitor<T> visitor) {
    return visitor.visitKey(this);
  }

  @Override
  public void pickedUp(Player player) {
    super.pickedUp(player);
    this.currentLocation = new Ejected(this.currentLocation);
    this.exit.unlock();
  }

}
