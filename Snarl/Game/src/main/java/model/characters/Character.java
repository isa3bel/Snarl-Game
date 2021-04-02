package model.characters;

import model.*;
import model.controller.Controller;
import model.controller.StdinController;
import model.level.Level;
import model.ruleChecker.Interactable;
import model.ruleChecker.Interaction;
import model.level.Location;
import model.ruleChecker.MoveValidator;

import java.util.ArrayList;

/**
 * A mobile character in the Snarl game.
 */
public abstract class Character implements Interactable {

  private final String name;
  protected Location currentLocation;
  protected Controller controller;

  Character(Location location, String name) {
    this(location, name, new StdinController(name));
  }

  Character(Location location, String name, Controller controller) {
    this.name = name;
    this.currentLocation = location;
    this.controller = controller;
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

  /**
   * Is this character still playing in the game?
   * @return whether the character is still playing
   */
  public boolean isInGame() {
    return this.currentLocation.isInLevel();
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
   * @param level the level of the interaction
   * @param players potential players in the interaction
   * @return the interaction object of this character
   */
  // TODO: hate that the interaction needs this, but it is to generate a random locatiton
  //  for the ghost - is there a better way to do this?
  //  other ideas: add a "generateRandomValidLocation" to controller
  public abstract Interaction makeInteraction(Level level, ArrayList<Player> players);

}
