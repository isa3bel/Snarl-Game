package model.interactions;

import model.characters.Adversary;
import model.characters.Player;
import model.level.Exit;
import model.GameManager;
import model.item.Item;

public class PlayerInteraction extends Interaction<Player> {

  private final GameManager gameManager;

  public PlayerInteraction(Player player, GameManager gameManager) throws IllegalArgumentException {
    super(player);

    if (gameManager == null) {
      throw new IllegalArgumentException("player interaction must have a game manager");
    }
    this.gameManager = gameManager;
  }

  @Override
  public void visitItem(Item item) {
    if (!item.getCurrentLocation().equals(this.character.getCurrentLocation())) return;
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
