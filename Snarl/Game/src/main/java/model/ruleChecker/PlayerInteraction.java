package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.level.Exit;
import model.item.Item;

/**
 * Controls a player interaction with an Interactable - what happens and when.
 */
public class PlayerInteraction extends Interaction<Player> {

  public PlayerInteraction(Player player) throws IllegalArgumentException {
    super(player);
  }

  @Override
  public void visitItem(Item item) {
    if (item.getCurrentLocation() == null ||
        !item.getCurrentLocation().equals(this.character.getCurrentLocation())) {
      return;
    }
    item.pickedUp(this.character);
  }

  @Override
  public void visitAdversary(Adversary adversary) {
    // self-elimination
    adversary.attack(this.character);
  }

  @Override
  public void visitExit(Exit exit) {
    if (exit.isLocked()) return;
    this.character.moveTo(null);
  }

}
