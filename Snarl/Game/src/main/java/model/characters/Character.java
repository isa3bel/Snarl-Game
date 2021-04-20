package model.characters;

import model.*;
import model.controller.Controller;
import model.level.Ejected;
import model.level.Level;
import model.ruleChecker.Interactable;
import model.ruleChecker.Interaction;
import model.level.Location;
import model.ruleChecker.MoveResult;
import model.ruleChecker.MoveValidator;
import view.View;

import java.util.ArrayList;

/**
 * A mobile character in the Snarl game.
 */
public abstract class Character implements Interactable {

  protected Location currentLocation;
  private final String name;
  protected Controller controller;
  private int health;
  private final int attack;

  Character(Location location, String name, int health, int attack) {
    this(location, name, health, attack, null);
  }

  Character(Location location, String name, int health, int attack, Controller controller) {
    this.currentLocation = location;
    this.name = name;
    this.health = health;
    this.attack = attack;
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
   * Makes this character attack another character
   * @param character the character to attack
   * @param reboundLocation the location that this character "rebounds" to after the attack
   * @return the result of the attack
   */
  public MoveResult attack(Character character, Location reboundLocation) {
    if (!this.currentLocation.equals(character.getCurrentLocation())) return MoveResult.OK;
    MoveResult result = character.defend(this.attack);
    if (!result.equals(MoveResult.EJECTED)) this.moveTo(reboundLocation);
    return result;
  }

  /**
   * Makes this character defend against the attack of another player.
   * @param attack the power of the attack that this character is defending against
   * @return the result of the defense
   */
  public MoveResult defend(int attack) {
    this.health -= attack;
    if (this.health <= 0) {
      System.out.println(this.getName() + " has been ejected.");
      this.currentLocation = new Ejected(this.currentLocation);
      return MoveResult.EJECTED;
    }
    System.out.println(this.getName() + " has " + this.health + " points left");
    return MoveResult.ATTACK;
  }

  /**
   * Gets the name of this character.
   * @return the character's name
   */
  public String getName() { return this.name; }

  /**
   * Updates this character with the given view.
   */
  public void updateController(GameManager gameManager) {
    View view = this.controller.getDefaultView(this);
    gameManager.buildView(view);
    this.controller.update(view);
  }

  /**
   * Updates this character with the given view.
   */
  public abstract void updateController(View view);

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
  public abstract Interaction makeInteraction(Level level, ArrayList<Player> players, Location initialLocation);

}
