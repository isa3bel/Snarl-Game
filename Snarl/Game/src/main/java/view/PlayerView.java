package view;

import model.characters.Character;
import model.level.Level;
import model.level.Location;
import model.level.SpaceVisitor;
import model.level.VoidWall;

import java.util.ArrayList;

public abstract class PlayerView implements View {

  protected final int VIEW_DISTANCE = 2;
  protected final String name;
  protected final Location playerLocation;
  protected final ArrayList<ArrayList<String>> render;
  private final SpaceVisitor<String> spaceVisitor;

  protected PlayerView(Character character, SpaceVisitor<String> spaceVisitor) {
    this.name = character.getName();
    this.playerLocation = character.getCurrentLocation();
    this.render = new ArrayList<>();
    this.spaceVisitor = spaceVisitor;

    for (int row = 0; row < VIEW_DISTANCE * 2 + 1; row++) {
      this.render.add(new ArrayList<>(VIEW_DISTANCE * 2 + 1));
      for (int idx = 0; idx < VIEW_DISTANCE * 2 + 1; idx++) {
        this.render.get(row).add(new VoidWall().acceptVisitor(spaceVisitor));
      }
    }
  }

  /**
   * Should the given location be in this PlayerView?
   * @param location the location that might be in the view
   * @return is this location in the VIEW_DISTANCE range
   */
  protected boolean shouldBeInView(Location location) {
    return location.isInLevel() &&
        Math.abs(this.playerLocation.getRow() - location.getRow()) <= VIEW_DISTANCE &&
        Math.abs(this.playerLocation.getColumn() - location.getColumn()) <= VIEW_DISTANCE;
  }

  /**
   * Adds all the tiles in the player's view to the render array.
   * @param level the level with the tiles to draw
   */
  protected void addTilesToRender(Level level) {
    level.filter((space, location) ->
        this.shouldBeInView(location)
    ).forEach((location, space) -> {
      int actualRow = location.getRow() - this.playerLocation.getRow() + VIEW_DISTANCE;
      int actualColumn = location.getColumn() - this.playerLocation.getColumn() + VIEW_DISTANCE;
      this.render.get(actualRow).set(actualColumn, space.acceptVisitor(spaceVisitor));
    });
  }

}
