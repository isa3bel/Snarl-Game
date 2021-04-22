package model.characters;

import model.controller.Controller;
import model.level.Level;
import model.level.Location;
import model.controller.AdversaryAIController;
import model.controller.GhostAI;
import model.ruleChecker.*;

import java.util.ArrayList;

/**
 * Adversary that can move on walls.
 */
public class Ghost extends Adversary {

  public Ghost(Location location, String name) {
    super(location, name, 6, 1);
    this.controller = new AdversaryAIController(GhostAI.class, loc -> new GhostMoveValidator(this, loc));
  }

  public Ghost(Location location, String name, Controller controller) {
    super(location, name, 6, 1, controller);
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
  public Interaction makeInteraction(Level level, ArrayList<Player> players, Location initialLocation) {
    Location randomLocation = level.calculateValidActorPositions().stream()
        .filter(location -> !location.equals(this.currentLocation)
            && players.stream().noneMatch(player -> player.getCurrentLocation().equals(location))
            && !location.equals(initialLocation)
            && !players.stream().anyMatch(player -> player.getCurrentLocation().equals(location)))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("no valid locations that ghost can teleport to"));
    return new GhostInteraction(this, initialLocation, randomLocation);
  }
}
