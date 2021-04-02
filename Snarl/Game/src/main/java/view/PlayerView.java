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
        this.render.get(row).add("X");
      }
    }
  }

  /**
   * Should the given location be in this PlayerView?
   * @param location the location that might be in the view
   * @return is this location in the VIEW_DISTANCE range
   */
  private boolean shouldBeInView(Location location) {
    return location.isInLevel() &&
        Math.abs(this.playerLocation.getRow() - location.getRow()) <= VIEW_DISTANCE &&
        Math.abs(this.playerLocation.getColumn() - location.getColumn()) <= VIEW_DISTANCE;
  }

  @Override
  public void renderLevel(Level level) {
    SpaceVisitor<String> layout = new ASCIISpace();
    level.filter((space, location) ->
      this.shouldBeInView(location)
    ).forEach((location, space) -> {
      int actualRow = location.getRow() - this.playerLocation.getRow() + VIEW_DISTANCE;
      int actualColumn = location.getColumn() - this.playerLocation.getColumn() + VIEW_DISTANCE;
      this.render.get(actualRow).set(actualColumn, space.acceptVisitor(layout));
    });
    level.interact(new PlayerASCIIInteraction(this.render, this.playerLocation, (l) -> this.shouldBeInView(l), VIEW_DISTANCE));
  }

  @Override
  public void placePlayers(List<Player> players) {
    players.forEach(player -> {
      Location location = player.getCurrentLocation();

      // second part of this is to make sure that the player themselves is not included
      // in the view (a different player can't be at the same location)
      if (this.shouldBeInView(location)) {
        player.acceptVisitor(new PlayerASCIIInteraction(this.render, this.playerLocation, (l) -> this.shouldBeInView(l), VIEW_DISTANCE));
      }
    });
  }

  @Override
  public void draw() {
    System.out.println(this.name + " is at location " + this.playerLocation);
    System.out.println(this.toString());
  }

  @Override
  public String toString() {
    String outputString = render.stream()
        .map(row -> String.join("", row))
        .collect(Collectors.joining("\n"));
    return outputString + "\n";
  }
}
