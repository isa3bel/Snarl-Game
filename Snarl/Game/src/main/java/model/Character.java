package model;

/**
 * A mobile character in the Snarl game.
 */
public abstract class Character implements Interactable {

  protected Location currentLocation;
  protected Controller controller;

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
    return this.currentLocation == null ? null : new Location(this.currentLocation);
  }

  /**
   * Move this character to the given location.
   * @param location the location to move to
   */
  public void moveTo(Location location) {
    this.currentLocation = location;
  }

  public abstract MoveValidator getNextMove();
  public abstract Interaction makeInteraction(GameManager gameManager);

}
