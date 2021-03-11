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

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitAdversary(this);
  }

  public void attack(Player player) {
    player.defend();
  }

  @Override
  public MoveValidator getNextMove() {
    // TODO: update the controller with the view of the game manager?
    //  if that happens here?
    Location nextLocation = this.controller.getNextMove();
    return new AdversaryMoveValidator(this, nextLocation);
  }

  @Override
  public Interaction makeInteraction(GameManager gameManager) {
    return new AdversaryInteraction(this);
  }

}
