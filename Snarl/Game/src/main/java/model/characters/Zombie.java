package model.characters;

import model.GameManager;
import model.level.Location;
import model.controller.AdversaryAIController;
import model.ruleChecker.MoveValidator;
import model.controller.ZombieAI;
import model.ruleChecker.ZombieMoveValidator;
import view.AdversaryAIView;

/**
 * A Zombie adversary in the Snarl game
 */
public class Zombie extends Adversary {

  public Zombie(Location location, String name) {
    super(location, name, new AdversaryAIController(ZombieAI.class));
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

  @Override
  public void updateController(GameManager gameManager) {
    AdversaryAIView view = new AdversaryAIView(this.currentLocation,
        location -> new ZombieMoveValidator(this, location));
    gameManager.buildView(view);
    this.controller.update(view);
  }
}
