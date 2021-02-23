package view;

import model.Character;
import model.Item;
import model.Level;

import java.util.List;

public interface View {

  void renderLevel(Level level);
  void placeCharacters(List<Character> character);
  void placeItems(List<Item> item);
  void draw();
}
