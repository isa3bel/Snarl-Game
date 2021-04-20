package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Location;

/**
 * Controls a player interaction with an Interactable - what happens and when.
 */
public class PlayerInteraction extends Interaction {

  private final Player player;
  private final Location initialLocation;

  public PlayerInteraction(Player player, Location initialLocation) throws IllegalArgumentException {
    if (player == null) {
      throw new IllegalArgumentException("player cannot be null");
    }
    this.player = player;
    this.initialLocation = initialLocation;
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
    return this.player.attack(adversary, this.initialLocation);
  }

  @Override
  public MoveResult visitExit(Exit exit) {
    if (exit.isLocked() || !exit.getCurrentLocation().equals(this.player.getCurrentLocation())) return null;
    this.player.exit();
    return MoveResult.EXITED;
  }

}
