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

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitItem(this);
  }

  public Location getCurrentLocation() {
    return this.currentLocation == null ? null : new Location(this.currentLocation);
  }

  public abstract void pickedUp(Player player);
}
