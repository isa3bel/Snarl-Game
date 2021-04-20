package model.characters;

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
   * @param health the the adversary's health
   * @param attack the adversary's attack power
   */
  protected Adversary(Location location, String name, int health, int attack) {
    super(location, name, health, attack);
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
  public Interaction makeInteraction(Level level, ArrayList<Player> players, Location initialLocation) {
    return new AdversaryInteraction(this, initialLocation);
  }

  @Override
  public void updateController(View view) {
    // Adversaries are not controlled by a user of the snarl game,
    // therefore they do not need this updateController method
  }
}
