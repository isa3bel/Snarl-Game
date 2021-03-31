package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Exited;

/**
 * Controls a player interaction with an Interactable - what happens and when.
 */
public class PlayerInteraction extends Interaction<Player> {

  public PlayerInteraction(Player player) throws IllegalArgumentException {
    super(player);
  }

  @Override
  public Void visitKey(Key key) {
    if (key.getCurrentLocation() == null
        || !key.getCurrentLocation().equals(this.character.getCurrentLocation())) {
      return null;
    }
    key.pickedUp(this.character);
    return null;
  }

  @Override
  public Void visitAdversary(Adversary adversary) {
    // self-elimination
    adversary.attack(this.character);
    return null;
  }

  @Override
  public Void visitExit(Exit exit) {
    if (exit.isLocked()) return null;
    this.character.moveTo(new Exited(this.character.getCurrentLocation()));
    return null;
  }

}
