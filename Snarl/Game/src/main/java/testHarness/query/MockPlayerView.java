package testHarness.query;

import model.characters.Character;
import model.characters.Player;
import model.level.Level;
import model.level.Location;
import view.PlayerView;
import view.View;

import java.util.List;

public class MockPlayerView implements View {

  private final String name;
  private final Location position;
  private final PlayerView playerView;

  MockPlayerView(Player player) {
    this.name = player.getName();
    this.position = player.getCurrentLocation();
    this.playerView = new PlayerView(player);
  }

  @Override
  public void renderLevel(Level level) {
    // TODO: get the objects
  }

  @Override
  public void placeCharacters(List<Character> character) {
    // TODO: get the actors
  }

  @Override
  public void draw() {
    System.out.println(this.toString());
  }

  @Override
  public String toString() {
    String playerUpdate = "{\n" +
        "  \"type\": \"player-update\",\n" +
        "  \"layout\": " + this.playerView.toString() + ",\n" +
        "  \"position\": " + this.position.toString() + ",\n" +
        "  \"objects\": [" + null + "],\n" + // TODO: this will be the list of objects from renderLevel
        "  \"actors\": [" + null + "]\n}"; // TODO: this will be the list of actors from placeCharacters
    return "[ " + this.name + ", " + playerUpdate + " ]";
  }
}
