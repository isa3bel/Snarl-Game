import model.GameManager;
import model.Level;
import model.LevelBuilder;
import model.RoomBuilder;
import view.ASCIIView;
import view.View;

public class SnarlRunner {

  public static void main(String[] args) {
    GameManager gameManager = this.makeGameManager();
    View view = new ASCIIView();
    gameManager.draw(view);
  }

  public static Level makeGameManager() {
    RoomBuilder room1 = new RoomBuilder(0,0,20,30).exit(0, 21);
    Level level = new LevelBuilder().room(room1).build();
    GameManager gameManager = new GameManager(level);
    return gameManager;
  }
}
