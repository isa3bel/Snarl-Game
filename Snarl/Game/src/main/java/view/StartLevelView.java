package view;

import model.characters.Character;
import model.characters.Player;
import model.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public class StartLevelView implements View {

  private final int levelIndex;
  private String players;

  public StartLevelView(int levelIndex) {
    this.levelIndex = levelIndex;
  }

  @Override
  public void renderLevel(Level level) {
    // nothing to draw from level
  }

  @Override
  public void placePlayers(List<Player> players) {
    this.players = "[ \"" + players.stream()
        .map(Character::getName)
        .collect(Collectors.joining("\", \"")) + "\" ]";
  }

  @Override
  public void draw() {
    System.out.print(this.toString());
  }

  public String toString() {
    return "{ \"type\": \"start-level\",\n"
        + "  \"level\": " + this.levelIndex + ",\n"
        + "  \"players\": " + this.players + "\n}";
  }
}
