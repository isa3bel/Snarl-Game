package model.controller;

import model.characters.Ghost;
import model.characters.Player;
import model.characters.Zombie;
import model.level.Level;
import model.level.Location;
import model.ruleChecker.GhostMoveValidator;
import model.ruleChecker.ZombieMoveValidator;
import org.junit.Test;
import view.View;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdversaryAIControllerTest {

  @Test
  public void testZombieAttacksPlayer() {
    Zombie zombie = new Zombie(new Location(7, 2), "zombie1");
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class,
        (loc) -> new ZombieMoveValidator(zombie, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 3],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testZombieGoesToPlayer() {
    Zombie zombie = new Zombie(new Location(7, 2), "zombie1");
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class,
        (loc) -> new ZombieMoveValidator(zombie, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 7],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testZombieGoesToKeyIfPlayerIsFar() {
    Zombie zombie = new Zombie(new Location(7, 2), "zombie1");
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class,
        (loc) -> new ZombieMoveValidator(zombie, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(6, 2));
  }

  @Test
  public void testZombieGoesToExitIfKeyIsNullAndPlayerIsFar() {
    Zombie zombie = new Zombie(new Location(7, 2), "zombie1");
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class,
        (loc) -> new ZombieMoveValidator(zombie, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": null,\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(8, 2));
  }

  @Test
  public void testGhostAttacksPlayer() {
    Ghost ghost = new Ghost(new Location(7, 2), "ghost1");
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class,
        (loc) -> new GhostMoveValidator(ghost, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 3],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testGhostGoesToPlayer() {
    Ghost ghost = new Ghost(new Location(7, 2), "ghost1");
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class,
        (loc) -> new GhostMoveValidator(ghost, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 7],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testGhostGoesToKeyIfPlayerIsFar() {
    Ghost ghost = new Ghost(new Location(7, 2), "ghost1");
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class,
        (loc) -> new GhostMoveValidator(ghost, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(6, 2));
  }

  @Test
  public void testGhostGoesToWallIfKeyIsNullAndPlayerIsFar() {
    Ghost ghost = new Ghost(new Location(7, 2), "ghost1");
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class,
        (loc) -> new GhostMoveValidator(ghost, loc));
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 1]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": null,\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 1));
  }

  /**
   * Mock view that just returns a string.
   */
  private static class MockView implements View {

    private final String viewString;

    MockView(String viewString) {
      this.viewString = viewString;
    }

    @Override
    public void renderLevel(Level level) {

    }

    @Override
    public void placePlayers(List<Player> players) {

    }

    @Override
    public void draw() {

    }

    @Override
    public String toString() {
      return this.viewString;
    }
  }
}
