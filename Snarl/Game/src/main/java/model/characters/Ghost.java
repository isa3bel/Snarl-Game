package model.characters;

import model.level.Location;

public class Ghost extends Adversary {

  /**
   * The location of this adversary.
   *
   * @param location the location to initialize this adversary
   */
  public Ghost(Location location, String name) {
    super(location, name);
  }

  @Override
  public <T> T acceptVisitor(AdversaryVisitor<T> visitor) {
    return visitor.visitGhost(this);
  }
}
