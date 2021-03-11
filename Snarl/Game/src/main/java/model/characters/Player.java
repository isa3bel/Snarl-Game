package model.characters;

import model.GameManager;
import model.interactions.InteractableVisitor;
import model.interactions.Interaction;
import model.interactions.PlayerInteraction;
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

  public void defend() {
    this.currentLocation = null;
  }

  @Override
  public MoveValidator getNextMove() {
    Location nextLocation = this.controller.getNextMove();
    return new PlayerMoveValidator(this, nextLocation);
  }

  @Override
  public Interaction makeInteraction() {
    return new PlayerInteraction(this);
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }

  @Override
  public void updateController(GameManager gameManager) {
    PlayerView view = new PlayerView();
    this.controller.update(view);
  }

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitPlayer(this);
  }

}
