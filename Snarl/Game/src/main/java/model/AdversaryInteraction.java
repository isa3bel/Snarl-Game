package model;

public class AdversaryInteraction extends Interaction<Adversary> {

  AdversaryInteraction(Adversary adversary) {
    super(adversary);
  }

  @Override
  public void visitPlayer(Player player) {
    this.character.attack(player);
  }

}
