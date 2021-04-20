package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.level.Location;

/**
 * Controls an adversary interaction with an Interactable - what happens and under what conditions.
 */
public class AdversaryInteraction extends Interaction {

  protected final Adversary adversary;
  private final Location initialLocation;

  public AdversaryInteraction(Adversary adversary, Location initialLocation) {
    this.adversary = adversary;
    this.initialLocation = initialLocation;
  }

  @Override
  public MoveResult visitPlayer(Player player) {
    return this.adversary.attack(player, this.initialLocation);
  }

}
