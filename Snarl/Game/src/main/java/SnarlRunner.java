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
    gameManager.buildView(view);

    System.out.println("\n");

    GameManager complicated = setupComplicatedLevel();
    complicated.buildView(view);
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
    Level level = new LevelBuilder().addRoom(room1).build();
    Level[] levels = { level };
    return new GameManagerBuilder(0, levels).build();
  }

  private static GameManager setupComplicatedLevel() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .addDoor(21, 4)
        .addDoor(16, 9)
        .addWall(1, 1)
        .addWall(2, 1)
        .addWall(5, 4)
        .addWall(6, 4)
        .addWall(7, 4)
        .addWall(5, 5)
        .addWall(6, 5)
        .addWall(7, 5);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .addDoor(24, 4);
    RoomBuilder room3 = new RoomBuilder(28, 12, 4, 2)
        .addDoor(27, 13);
    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway2 = new HallwayBuilder(room1, room3).addWaypoint(16, 13);
    Level twoRoomsLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addRoom(room3)
        .addHallway(hallway1)
        .addHallway(hallway2)
        .build();
    Level[] levels = { twoRoomsLevel };
    return new GameManagerBuilder(0, levels).build();
  }

}
