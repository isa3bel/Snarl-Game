package model.characters;

import model.level.Location;

/**
 * A Zombie adversary in the Snarl game
 */
public class Zombie extends Adversary {

  public Zombie(Location location, String name) {
    super(location, name);
  }

  @Override
  public <T> T acceptVisitor(AdversaryVisitor<T> visitor) {
    return visitor.visitZombie(this);
  }
}
