package view;

import model.characters.Player;
import model.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EndGameView implements View {

  private final boolean didWin;
  private String playerScores;

  public EndGameView(boolean didWin) {
    this.didWin = didWin;
  }

  @Override
  public void renderLevel(Level level) {
    // nothing to render from the level
  }

  @Override
  public void placePlayers(List<Player> players) {
    Collections.sort(players);
    this.playerScores = "[ " + players.stream().map(Player::score).collect(Collectors.joining(", ")) + " ]";
  }

  @Override
  public void draw() {
    System.out.println(this);
  }

  @Override
  public String toString() {
    return "{ \"type\": \"end-game\",\n  \"scores\": " + this.playerScores
        + ",\n  \"game-won\": " + this.didWin + "\n}";
  }
}
