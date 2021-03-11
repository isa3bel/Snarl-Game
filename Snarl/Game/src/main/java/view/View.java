package view;

import model.characters.Character;
import model.level.Level;

import java.util.List;

public interface View {

  void renderLevel(Level level);
  void placeCharacters(List<Character> character);
  void draw();
}
