package view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.characters.Character;
import model.level.Level;
import model.level.Location;

// should be similar to Adversary view in that an Observer can see everything (for debugging purposes)
public class ObserverView implements View {

  private ASCIIView view;

  public ObserverView() {
    view = new ASCIIView();
  }

  @Override
  public void renderLevel(Level level) {
    this.view.renderLevel(level);
  }

  @Override
  public void placeCharacters(List<Character> character) {
    this.view.placeCharacters(character);
  }

  @Override
  public void draw() {
    this.view.draw();
  }

  public String toString() {
    return this.view.toString();
  }
}
