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
    RoomBuilder room1 = new RoomBuilder(0,0,20,30).exit(0, 30);
    Level level = new LevelBuilder().room(room1).build();
    GameManager gameManager = new GameManager(level);
    return gameManager;
  }
}
