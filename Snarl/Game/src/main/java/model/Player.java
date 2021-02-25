package model;

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
  Player(Location location, int id) {
    super(location);
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
  public Interaction makeInteraction(GameManager gameManager) {
    return new PlayerInteraction(this, gameManager);
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