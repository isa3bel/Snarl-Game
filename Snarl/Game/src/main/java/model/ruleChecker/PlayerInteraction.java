package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Exited;

/**
 * Controls a player interaction with an Interactable - what happens and when.
 */
public class PlayerInteraction extends Interaction {

  private final Player player;

  public PlayerInteraction(Player player) throws IllegalArgumentException {
    this.player = player;
  }

  @Override
  public MoveResult visitKey(Key key) {
    if (key.getCurrentLocation() == null
        || !key.getCurrentLocation().equals(this.player.getCurrentLocation())) {
      return null;
    }
    key.pickedUp(this.player);
    return MoveResult.KEY;
  }

  @Override
  public MoveResult visitAdversary(Adversary adversary) {
    // self-elimination
    return adversary.attack(this.player);
  }

  @Override
  public MoveResult visitExit(Exit exit) {
    if (exit.isLocked() || !exit.getCurrentLocation().equals(this.player.getCurrentLocation())) return null;
    this.player.exit();
    return MoveResult.EXITED;
  }

}
