package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;

/**
 * Controls an adversary interaction with an Interactable - what happens and under what conditions.
 */
public class AdversaryInteraction extends Interaction<Adversary> {

  public AdversaryInteraction(Adversary adversary) {
    super(adversary);
  }

  @Override
  public void visitPlayer(Player player) {
    this.character.attack(player);
  }

}
