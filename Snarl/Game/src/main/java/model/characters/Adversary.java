package model.characters;

import model.GameManager;
import model.ruleChecker.*;
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
    super(location, name, null);
  }

  /**
   * Attacks the given character.
   * @param player the player to attack
   */
  public void attack(Player player) {
    if (this.currentLocation.equals(player.getCurrentLocation())) {
      player.defend();
    }
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
  public AdversaryMoveValidator getNextMove() {
    // TODO: take this out when adversary is fully implemented
    if (this.controller == null) return new AdversaryMoveValidator(this, this.currentLocation);

    Location nextLocation = this.controller.getNextMove();
    return new AdversaryMoveValidator(this, nextLocation);
  }

  @Override
  public Interaction<Adversary> makeInteraction() {
    return new AdversaryInteraction(this);
  }

  @Override
  public void updateController(GameManager gameManager) {
    // TODO: take this out when adversary is fully implemented
    if (this.controller == null) return;

    AdversaryView view = new AdversaryView();
    this.controller.update(view);
  }

}
