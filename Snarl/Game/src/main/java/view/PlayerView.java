package view;

import model.characters.Player;
import model.level.Level;
import model.level.Location;
import model.level.SpaceVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A view of the game state from the perspective of a player.
 */
public class PlayerView implements View {

  private final int VIEW_DISTANCE = 2;
  private final String name;
  private final Location playerLocation;
  private final ArrayList<ArrayList<String>> render;
  private final StringBuilder objects;
  private final StringBuilder actors;

  public PlayerView(Player player) {
    this.name = player.getName();
    this.playerLocation = player.getCurrentLocation();
    this.render = new ArrayList<>();
    this.objects = new StringBuilder();
    this.actors = new StringBuilder();
    
    for (int row = 0; row < VIEW_DISTANCE * 2 + 1; row++) {
      this.render.add(new ArrayList<>(VIEW_DISTANCE * 2 + 1));
      for (int idx = 0; idx < VIEW_DISTANCE * 2 + 1; idx++) {
        this.render.get(row).add("0");
      }
    }
  }

  /**
   * Should the given location be in this PlayerView?
   * @param location the location that might be in the view
   * @return is this location in the VIEW_DISTANCE range
   */
  private boolean shouldBeInView(Location location) {
    return location != null &&
        Math.abs(this.playerLocation.getRow() - location.getRow()) <= VIEW_DISTANCE &&
        Math.abs(this.playerLocation.getColumn() - location.getColumn()) <= VIEW_DISTANCE;
  }

  @Override
  public void renderLevel(Level level) {
    SpaceVisitor<String> layout = new LayoutSpace();
    level.filter((space, location) ->
      this.shouldBeInView(location)
    ).forEach((location, space) -> {
      int actualRow = location.getRow() - this.playerLocation.getRow() + VIEW_DISTANCE;
      int actualColumn = location.getColumn() - this.playerLocation.getColumn() + VIEW_DISTANCE;
      this.render.get(actualRow).set(actualColumn, space.acceptVisitor(layout));
    });
    level.interact(new JSONInteraction(this.objects, this.actors, this::shouldBeInView));
  }

  @Override
  public void placePlayers(List<Player> players) {
    players.forEach(player -> {
      Location location = player.getCurrentLocation();

      // second part of this is to make sure that the player themselves is not included
      // in the view (a different player can't be at the same location)
      if (this.shouldBeInView(location) && !location.equals(this.playerLocation)) {
        String delimiter = this.actors.length() == 0 ? "" : ",\n";
        String playerJson = "{\n  \"type\": \"player\",\n  \"name\": \"" + player.getName() + "\",\n  \"position\": "
            + location.toString() + "\n}";
        this.actors.append(delimiter).append(playerJson);
      }
    });
  }

  @Override
  public void draw() {
    System.out.println(this.toString());
  }

  @Override
  public String toString() {
    String layout = render.stream()
        .map(row -> "[ " + String.join(", ", row) + " ]")
        .collect(Collectors.joining(",\n"));

    return "[ \"" + this.name + "\", {\n  \"type\": \"player-update\",\n" +
        "  \"layout\": [" + layout + "],\n" +
        "  \"position\": " + this.playerLocation.toString() + ",\n" +
        "  \"objects\": [" + String.join(", ", this.objects) + "],\n" +
        "  \"actors\": [" + String.join(", ", this.actors) + "]\n} ]\n";
  }
}
