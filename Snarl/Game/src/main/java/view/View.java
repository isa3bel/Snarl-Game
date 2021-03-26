package view;

import model.characters.Player;
import model.level.Level;

import java.util.List;

public interface View {

  void renderLevel(Level level);
  void placePlayers(List<Player> players);
  void draw();
}
