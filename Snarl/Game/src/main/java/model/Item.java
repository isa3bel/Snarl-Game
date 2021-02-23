package model;

/**
 * An item in a Snarl dungeon crawler.
 */
public abstract class Item {

  Location currentLocation;

  Item(Location location) {
    this.currentLocation = location;
  }

  public abstract <T> T acceptVisitor(ItemVisitor<T> visitor);

  public Location getCurrentLocation() {
    return this.currentLocation;
  }
}
