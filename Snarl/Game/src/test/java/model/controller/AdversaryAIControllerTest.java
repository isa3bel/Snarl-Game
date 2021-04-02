package model.controller;

import model.characters.Player;
import model.level.Level;
import model.level.Location;
import org.junit.Test;
import view.View;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdversaryAIControllerTest {

  @Test
  public void testZombieAttacksPlayer() {
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 3],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testZombieGoesToPlayer() {
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 7],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testZombieGoesToKeyIfPlayerIsFar() {
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(6, 2));
  }

  @Test
  public void testZombieGoesToExitIfKeyIsNullAndPlayerIsFar() {
    AdversaryAIController aiController = new AdversaryAIController(ZombieAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": null,\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(8, 2));
  }

  @Test
  public void testGhostAttacksPlayer() {
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 3],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testGhostGoesToPlayer() {
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 7],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(7, 3));
  }

  @Test
  public void testGhostGoesToKeyIfPlayerIsFar() {
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class);
    aiController.update(new MockView("{\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [5, 20],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [9, 2],\n" +
        "\"closestWall\": [7, 0]}"));
    assertEquals(aiController.getNextMove(), new Location(6, 2));
  }

  @Test
  public void testGhostGoesToWallIfKeyIsNullAndPlayerIsFar() {
    AdversaryAIController aiController = new AdversaryAIController(GhostAI.class);
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
