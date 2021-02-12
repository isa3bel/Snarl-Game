import model.GameManager;
import model.Level;
import model.LevelBuilder;
import model.RoomBuilder;
import view.ASCIIView;
import view.View;

/**
 * Runs the Snarl game.
 */
public class SnarlRunner {

  public static void main(String[] args) {
    GameManager gameManager = makeGameManager();
    View view = new ASCIIView();
    gameManager.draw(view);
  }

  /**
   * Makes the game to run.
   * @return the game
   */
  public static GameManager makeGameManager() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .exit(0, 6)
        .wall(1,1)
        .wall(2,1)
        .wall(5,4)
        .wall(6,4)
        .wall(7,4)
        .wall(5,5)
        .wall(6,5)
        .wall(7,5);
    Level level = new LevelBuilder().room(room1).build();
    GameManager gameManager = new GameManager(level);
    return gameManager;
  }
}
