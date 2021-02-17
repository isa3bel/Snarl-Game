import model.*;
import view.ASCIIView;
import view.View;

/**
 * Runs the Snarl game.
 */
public class SnarlRunner {

  public static void main(String[] args) {
    View view = new ASCIIView();
    GameManager gameManager = makeGameManager();
    gameManager.draw(view);

    System.out.println("\n");

    GameManager complicated = setupComplicatedLevel();
    complicated.draw(view);
  }

  /**
   * Makes the game to run.
   * @return the game
   */
  public static GameManager makeGameManager() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .addWall(1,1)
        .addWall(2,1)
        .addWall(5,4)
        .addWall(6,4)
        .addWall(7,4)
        .addWall(5,5)
        .addWall(6,5)
        .addWall(7,5);
    Level level = new LevelBuilder().room(room1).build();
    GameManager gameManager = new GameManager(level);
    return gameManager;
  }

  private static GameManager setupComplicatedLevel() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .door(21, 4)
        .door(16, 9)
        .addWall(1, 1)
        .addWall(2, 1)
        .addWall(5, 4)
        .addWall(6, 4)
        .addWall(7, 4)
        .addWall(5, 5)
        .addWall(6, 5)
        .addWall(7, 5);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4);
    RoomBuilder room3 = new RoomBuilder(28, 12, 4, 2)
        .door(27, 13);
    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway2 = new HallwayBuilder(room1, room3).waypoint(16, 13);
    Level twoRoomsLevel = new LevelBuilder()
        .room(room1)
        .room(room2)
        .room(room3)
        .hallway(hallway1)
        .hallway(hallway2)
        .build();
    return new GameManager(twoRoomsLevel);
  }

}
