package model.characters;

import model.GameManager;
import model.item.Item;
import model.ruleChecker.*;
import model.level.Location;
import view.PlayerView;

/**
 * A player controller by a user in the Snarl dungeon crawler.
 */
public class Player extends Character {

  // TODO: do we want this to be public?
  public int id;

  /**
   * Creates a player at the given location with the given id.
   * @param location the location to initialize the player at
   * @param id the player's id
   */
  public Player(Location location, int id, String name) {
    super(location, name);
    this.id = id;
  }

  /**
   * Creates a player at the given location with the given id and controller.
   * @param location the location to initialize the player at
   * @param id the player's id
   */
  public Player(Location location, int id, String name, Controller controller) {
    super(location, name, controller);
    this.id = id;
  }

  /**
   * Updates the player after defending against an attack from an adversary.
   */
  public void defend() {
    this.currentLocation = null;
  }

  public void addToInventory(Item item) {
    // right now a player doesn't need an inventory, so this isn't necessary
    // TODO: THIS IS ONLY USED IN THE MILESTONE 7 TESTING TASK
  }

  @Override
  public PlayerMoveValidator getNextMove() {
    Location nextLocation = this.controller.getNextMove();
    return new PlayerMoveValidator(this, nextLocation);
  }

  @Override
  public Interaction<Player> makeInteraction() {
    return new PlayerInteraction(this);
  }

  @Override
  public void updateController(GameManager gameManager) {
    PlayerView view = new PlayerView(this);
    this.controller.update(view);
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitPlayer(this);
  }

}
