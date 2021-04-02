package model.characters;

import model.GameManager;
import model.controller.Controller;
import model.level.Level;
import model.ruleChecker.*;
import model.level.Location;

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
  Adversary(Location location, String name, Controller controller) {
    super(location, name, controller);
  }

  /**
   * Attacks the given character.
   * @param player the player to attack
   */
  public void attack(Player player) {
    if (this.currentLocation.equals(player.getCurrentLocation())) {
      player.defend();
    }
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitAdversary(this);
  }
  public abstract <T> T acceptVisitor(AdversaryVisitor<T> visitor);

  @Override
  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitAdversary(this);
  }

  @Override
  public abstract MoveValidator getNextMove();

  @Override
  public Interaction<Adversary> makeInteraction(Level level, ArrayList<Player> players) {
    return new AdversaryInteraction(this);
  }

  @Override
  public abstract void updateController(GameManager gameManager);
}
