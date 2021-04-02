package model.item;

import model.ruleChecker.Interactable;
import model.ruleChecker.InteractableVisitor;
import model.characters.Player;
import model.level.Location;

/**
 * An item in a Snarl dungeon crawler.
 */
public abstract class Item implements Interactable {

  protected Location currentLocation;

  Item(Location location) {
    this.currentLocation = location;
  }

  public abstract <T> T acceptVisitor(ItemVisitor<T> visitor);

  public abstract void acceptVisitor(InteractableVisitor visitor);

  public Location getCurrentLocation() {
    return this.currentLocation;
  }

  public void pickedUp(Player player) {
    player.addToInventory(this);
  }
}
