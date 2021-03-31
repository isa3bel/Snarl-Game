package model.level;

import java.util.ArrayList;

/**
 * A location once a player has been ejected from the level.
 */
public class Ejected extends Location {

  public Ejected(Location location) throws IllegalArgumentException {
    super(location);
  }

  /**
   * Is this location in a level?
   * @return false
   */
  public boolean isInLevel() {
    return false;
  }

  /**
   * Was this player ejected from the level?
   * @return true
   */
  public boolean isDead() {
    return true;
  }

  @Override
  public ArrayList<Location> to(Location end) {
    return new ArrayList<>();
  }

  @Override
  public boolean isAdjacentTo(Location that) {
    return false;
  }

  @Override
  public String toString() {
    return "ejected";
  }
}
