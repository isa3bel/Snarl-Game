package view;

import model.characters.Character;
import model.level.Level;

import java.util.List;

/**
 * A view of the game state from the perspective of a player.
 */
public class AdversaryView implements View {

  private ASCIIView view;

  public AdversaryView() {
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
