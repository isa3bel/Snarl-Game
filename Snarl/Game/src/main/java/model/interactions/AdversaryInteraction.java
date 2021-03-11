package model.interactions;

import model.characters.Adversary;
import model.characters.Player;

public class AdversaryInteraction extends Interaction<Adversary> {

  public AdversaryInteraction(Adversary adversary) {
    super(adversary);
  }

  @Override
  public void visitPlayer(Player player) {
    this.character.attack(player);
  }

}
