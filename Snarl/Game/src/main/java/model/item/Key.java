package model.item;

import model.characters.Player;
import model.level.Ejected;
import model.level.Location;
import model.ruleChecker.InteractableVisitor;

/**
 * A key for a level exit in the Snarl game.
 */
public class Key extends Item {

  private final Exit exit;
  private String retrievedBy;

  public Key(Location loc, Exit exit) {
    super(loc);
    this.exit = exit;
  }

  @Override
  public <T> T acceptVisitor(InteractableVisitor<T> visitor) {
    return visitor.visitKey(this);
  }

  public <T> T acceptVisitor(ItemVisitor<T> visitor) {
    return visitor.visitKey(this);
  }

  @Override
  public void pickedUp(Player player) {
    super.pickedUp(player);
    this.currentLocation = new Ejected(this.currentLocation);
    this.exit.unlock();
    this.retrievedBy = player.getName();
  }

  /**
   * Who this key was retrieved by.
   * @return the retriever of this key
   */
  public String getRetrievedBy() {
    return this.retrievedBy;
  }

}
