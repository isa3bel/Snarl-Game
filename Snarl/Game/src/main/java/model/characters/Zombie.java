package model.characters;

import model.level.Location;
import model.controller.AdversaryAIController;
import model.ruleChecker.MoveValidator;
import model.controller.ZombieAI;
import model.ruleChecker.ZombieMoveValidator;

/**
 * A Zombie adversary in the Snarl game
 */
public class Zombie extends Adversary {

  public Zombie(Location location, String name) {
    super(location, name, 4, 2);
    this.controller = new AdversaryAIController(ZombieAI.class,
        loc -> new ZombieMoveValidator(this, loc));
  }

  @Override
  public <T> T acceptVisitor(AdversaryVisitor<T> visitor) {
    return visitor.visitZombie(this);
  }

  @Override
  public MoveValidator getNextMove() {
    Location nextMove = this.controller.getNextMove();
    return new ZombieMoveValidator(this, nextMove);
  }
}
