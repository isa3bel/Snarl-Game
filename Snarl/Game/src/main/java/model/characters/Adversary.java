package model.characters;

import model.GameManager;
import model.controller.Controller;
import model.level.Level;
import model.ruleChecker.*;
import model.level.Location;
import view.View;

import java.util.ArrayList;

/**
 * An automated adversary in a Snarl game.
 */
public abstract class Adversary extends Character {

  /**
   * The location of this adversary.
   * @param location the location to initialize this adversary
   * @param name arbitrary unused string for the name of the adversary
   */
  protected Adversary(Location location, String name) {
    super(location, name);
  }

  /**
   * Attacks the given character.
   * @param player the player to attack
   */
  public MoveResult attack(Player player) {
    if (this.currentLocation.equals(player.getCurrentLocation())) {
      player.defend();
      return MoveResult.EJECTED;
    }
    return MoveResult.OK;
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitAdversary(this);
  }
  public abstract <T> T acceptVisitor(AdversaryVisitor<T> visitor);

  @Override
  public <T> T acceptVisitor(InteractableVisitor<T> visitor) {
    return visitor.visitAdversary(this);
  }

  @Override
  public Interaction makeInteraction(Level level, ArrayList<Player> players) {
    return new AdversaryInteraction(this);
  }

  @Override
  public void updateController(View view) {
    // Adversaries are not controlled by a user of the snarl game,
    // therefore they do not need this updateController method
  }
}
