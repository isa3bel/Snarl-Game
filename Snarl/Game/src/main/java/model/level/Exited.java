package model.level;

import java.util.ArrayList;

/**
 * A location once a player has exited from the level.
 */
public class Exited extends Location {

  public Exited(Location location) throws IllegalArgumentException {
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
   * @return false
   */
  public boolean isDead() {
    return false;
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
    return "exited";
  }
}
