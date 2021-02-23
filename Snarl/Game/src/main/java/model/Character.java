package model;

/**
 * A mobile character in the Snarl game.
 */
public abstract class Character {

  private Location currentLocation;
  Controller controller;

  Character(Location location) {
    this.currentLocation = location;
  }

  /**
   * Accepts a Character visitor.
   * @param visitor visitor for this character
   * @param <T> the return of the visitor object
   * @return the visitor applied to this character
   */
  public abstract <T> T acceptVisitor(CharacterVisitor<T> visitor);

  /**
   * Gets the current location of this character.
   * @return the location of the character
   */
  public Location getCurrentLocation() {
    return this.currentLocation;
  }

}
