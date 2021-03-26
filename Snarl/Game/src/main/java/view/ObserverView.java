package view;

import java.util.List;
import model.characters.Player;
import model.level.Level;

// should be similar to Adversary view in that an Observer can see everything (for debugging purposes)
public class ObserverView implements View {

  private final ASCIIView view;

  public ObserverView() {
    view = new ASCIIView();
  }

  @Override
  public void renderLevel(Level level) {
    this.view.renderLevel(level);
  }

  @Override
  public void placePlayers(List<Player> players) {
    this.view.placePlayers(players);
  }

  @Override
  public void draw() {
    this.view.draw();
  }

  public String toString() {
    return this.view.toString();
  }
}
