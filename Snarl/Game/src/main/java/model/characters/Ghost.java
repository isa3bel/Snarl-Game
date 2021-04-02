package model.characters;

import model.GameManager;
import model.level.Level;
import model.level.Location;
import model.controller.AdversaryAIController;
import model.controller.GhostAI;
import model.ruleChecker.*;
import view.AdversaryAIView;

import java.util.ArrayList;

/**
 * Adversary that can move on walls.
 */
public class Ghost extends Adversary {

  /**
   * The location of this adversary.
   *
   * @param location the location to initialize this adversary
   */
  public Ghost(Location location, String name) {
    super(location, name, new AdversaryAIController(GhostAI.class));
  }

  @Override
  public <T> T acceptVisitor(AdversaryVisitor<T> visitor) {
    return visitor.visitGhost(this);
  }

  @Override
  public MoveValidator getNextMove() {
    Location nextMove = this.controller.getNextMove();
    return new GhostMoveValidator(this, nextMove);
  }

  @Override
  public void updateController(GameManager gameManager) {
    AdversaryAIView view = new AdversaryAIView(this.currentLocation,
        location -> new GhostMoveValidator(this, location));
    gameManager.buildView(view);
    this.controller.update(view);
  }

  @Override
  public Interaction<Adversary> makeInteraction(Level level, ArrayList<Player> players) {
    Location randomLocation = level.calculateValidActorPositions().stream()
        .filter(location -> !location.equals(this.currentLocation)
            && !players.stream().anyMatch(player -> player.getCurrentLocation().equals(location)))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("no valid locations that ghost can teleport to"));
    return new GhostInteraction(this, randomLocation);
  }
}
