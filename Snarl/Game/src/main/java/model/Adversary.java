package model;

/**
 * An automated adversary in a Snarl game.
 */
public class Adversary extends Character {

  /**
   * The location of this adversary.
   * @param location the location to initialize this adversary
   */
  Adversary(Location location) {
    super(location);
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitAdversary(this);
  }
}
