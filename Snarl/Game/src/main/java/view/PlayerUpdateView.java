package view;

import model.characters.Character;
import model.characters.Player;
import model.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerUpdateView extends PlayerView {

  private final StringBuilder actorString;
  private final StringBuilder objectString;
  private final String message;

  public PlayerUpdateView(Character character, String message) {
    super(character, new LayoutSpace());
    this.actorString = new StringBuilder();
    this.objectString = new StringBuilder();
    this.message = message;
  }

  @Override
  public void renderLevel(Level level) {
    this.addTilesToRender(level);
    level.interact(new JSONInteraction(this.objectString, this.actorString, this::shouldBeInView), null);
  }

  @Override
  public void placePlayers(List<Player> players) {
    // DECISION: player updates won't include the player themselves in the view
    players.forEach(player -> {
      // we know that if the player shares the same location, it is the same player bc players
      // cannot share spaces with other players
      if (player.getCurrentLocation().equals(this.playerLocation)) return;

      player.acceptVisitor(new JSONInteraction(this.objectString, this.actorString,
          this::shouldBeInView));
    });
  }

  @Override
  public void draw() {
    System.out.println(this.toString());
  }

  @Override
  public String toString() {
    String layoutString = this.render.stream()
        .map(row -> "[ " + String.join(", ", row) + " ]")
        .collect(Collectors.joining(", \n"));
    return "{ \"type\": \"player-update\",\n" +
        "  \"layout\": [ " + layoutString + " ],\n" +
        "  \"position\": " + this.playerLocation + ",\n" +
        "  \"objects\": [ " + this.objectString + " ],\n" +
        "  \"actors\": [ " + this.actorString + " ],\n" +
        "  \"message\": " + this.message + "\n }";
  }
}
