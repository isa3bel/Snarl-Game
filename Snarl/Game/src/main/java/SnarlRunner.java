import model.*;
import model.builders.GameManagerBuilder;
import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.level.Level;
import model.level.Location;
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
    while(true) {
      gameManager.doRound();
    }
  }

  /**
   * Makes the game to run.
   * @return the game
   */
  public static GameManager makeGameManager() {
    RoomBuilder room1 = new RoomBuilder(new Location(1,1),20,8)
        .addWall(new Location(1,1))
        .addWall(new Location(2,1))
        .addWall(new Location(5,4))
        .addWall(new Location(6,4))
        .addWall(new Location(7,4))
        .addWall(new Location(5,5))
        .addWall(new Location(6,5))
        .addWall(new Location(7,5));
    Level level = new LevelBuilder()
        .addRoom(room1)
        .addExit(new Location(0, 6))
        .build();
    Level[] levels = { level };
    return new GameManagerBuilder(0, levels).addPlayer().build();
  }

  private static GameManager setupComplicatedLevel() {
    RoomBuilder room1 = new RoomBuilder(new Location(1,1),20,8)
        .addDoor(new Location(9, 0))
        .addDoor(new Location(0, 0))
        .addWall(new Location(1, 1))
        .addWall(new Location(2, 1))
        .addWall(new Location(5, 4))
        .addWall(new Location(6, 4))
        .addWall(new Location(7, 4))
        .addWall(new Location(5, 5))
        .addWall(new Location(6, 5))
        .addWall(new Location(7, 5));
    RoomBuilder room2 = new RoomBuilder(new Location(25, 3), 10, 3)
        .addDoor(new Location(24, 3));
    RoomBuilder room3 = new RoomBuilder(new Location(28, 12), 4, 2)
        .addDoor(new Location(28, 11));

    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2, null);
    Location[] hallway2Waypoints = new Location[]{new Location(16, 13)};
    HallwayBuilder hallway2 = new HallwayBuilder(room1, room3, hallway2Waypoints);

    Level twoRoomsLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addRoom(room3)
        .addHallway(hallway1)
        .addHallway(hallway2)
        .addExit(new Location(0, 6))
        .build();
    Level[] levels = { twoRoomsLevel };
    return new GameManagerBuilder(0, levels).build();
  }

}
