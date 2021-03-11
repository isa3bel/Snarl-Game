package model.characters;

import model.GameManager;
import model.interactions.AdversaryInteraction;
import model.interactions.InteractableVisitor;
import model.interactions.Interaction;
import model.level.Location;
import view.AdversaryView;

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

  /**
   * Attacks the given character.
   * @param player the player to attack
   */
  public void attack(Player player) {
    if (this.currentLocation.equals(player.getCurrentLocation()))
      player.defend();
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

  @Override
  public MoveValidator<Adversary> getNextMove() {
    // TODO: update the controller with the view of the game manager?
    //  if that happens here?
    Location nextLocation = this.controller.getNextMove();
    return new AdversaryMoveValidator(this, nextLocation);
  }

  @Override
  public Interaction<Adversary> makeInteraction() {
    return new AdversaryInteraction(this);
  }

  @Override
  public void updateController(GameManager gameManager) {
    AdversaryView view = new AdversaryView();
    this.controller.update(view);
  }

}
