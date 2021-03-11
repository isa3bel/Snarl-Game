package model;

/**
 * An automated adversary in a Snarl game.
 */
public abstract class Adversary extends Character {

  /**
   * The location of this adversary.
   * @param location the location to initialize this adversary
   * @param name arbitrary unused string for the name of the adversary
   */
  Adversary(Location location, String name) {
    super(location, name);
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitAdversary(this);
  }
  public abstract <T> T acceptVisitor(AdversaryVisitor<T> visitor);

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitAdversary(this);
  }

  public void attack(Player player) {
    if (this.currentLocation.equals(player.getCurrentLocation()))
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
