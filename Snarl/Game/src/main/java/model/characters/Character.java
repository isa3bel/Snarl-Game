package model.characters;

import model.*;
import model.interactions.Interactable;
import model.interactions.Interaction;
import model.level.Location;

/**
 * A mobile character in the Snarl game.
 */
public abstract class Character implements Interactable {

  private final String name;
  protected Location currentLocation;
  protected Controller controller;

  Character(Location location, String name) {
    this.currentLocation = location;
    this.name = name;
    this.controller = new StdinController();
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

  /**
   * Gets the name of this character.
   * @return the character's name
   */
  public String getName() { return this.name; }

  /**
   * Updates this character with the current game state.
   */
  public abstract void updateController(GameManager gameManager);

  /**
   * Gets the next move as validated by this characters rules.
   * @return the move validator for the next move of this character
   */
  public abstract MoveValidator getNextMove();

  /**
   * Makes a character interaction for this character.
   * @return the interaction object of this character
   */
  public abstract Interaction makeInteraction();

}
