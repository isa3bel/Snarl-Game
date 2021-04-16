package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;

/**
 * Controls an adversary interaction with an Interactable - what happens and under what conditions.
 */
public class AdversaryInteraction extends Interaction {

  protected final Adversary adversary;

  public AdversaryInteraction(Adversary adversary) {
    this.adversary = adversary;
  }

  @Override
  public MoveResult visitPlayer(Player player) {
    this.adversary.attack(player);
    return MoveResult.EJECTED;
  }

}
