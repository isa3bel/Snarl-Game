package model.remote;

import model.level.Location;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayerUpdateViewer {

  private final Location position;
  private final LevelObject[] objects;
  private final ActorPosition[] actors;
  private final int[][] layout;
  private final String message;

  private PlayerUpdateViewer(Location position, LevelObject[] objects, ActorPosition[] actors, int[][] layout,
                             String message) {
    this.position = position;
    this.objects = objects;
    this.actors = actors;
    this.layout = layout;
    this.message = message;
  }

  /**
   * Builds the view for this level in the tiles.
   * @return a 2d array of the space viewable in this level
   */
  private ArrayList<ArrayList<String>> buildTiles() {
    ArrayList<ArrayList<String>> asciiLayout = new ArrayList<>();
    for (int[] row : layout) {
      ArrayList<String> asciiRow = new ArrayList<>();
      for (int col : row) {
        switch (col) {
          case 0:
            asciiRow.add("X"); break;
          case 1:
            asciiRow.add(" "); break;
          case 2:
            asciiRow.add("D"); break;
          default:
            throw new IllegalArgumentException("received invalid layout tile: " + col);
        }
      }
      asciiLayout.add(asciiRow);
    }
    return asciiLayout;
  }

  /**
   * Sets the viewable objects in the given view 2d array.
   * @param view the 2d array list representing the viewable spaces of the level
   */
  private void addObjects(ArrayList<ArrayList<String>> view) {
    for (LevelObject object : this.objects) {
      String representation = object.type.substring(0, 1).toUpperCase();
      view.get(object.position.getRow() - this.position.getRow() + this.layout.length / 2)
          .set(object.position.getColumn() - this.position.getColumn() + this.layout.length / 2, representation);
    }
  }

  /**
   * Sets the viewable actors in the given view 2d array.
   * @param view the 2d array list representing the viewable spaces of the level
   */
  private void addActors(ArrayList<ArrayList<String>> view) {
    for (ActorPosition actor : this.actors) {
      String representation = actor.type.substring(0, 1).toUpperCase();
      view.get(actor.position.getRow() - this.position.getRow() + this.layout.length / 2)
          .set(actor.position.getColumn() - this.position.getColumn() + this.layout.length / 2, representation);
    }
  }

  @Override
  public String toString() {
    ArrayList<ArrayList<String>> view = this.buildTiles();
    this.addObjects(view);
    this.addActors(view);
    view.get(this.layout.length / 2).set(this.layout.length / 2, "+");
    String levelLayout = view.stream()
        .map(row -> String.join("", row))
        .collect(Collectors.joining("\n"));
    return "You are currently at: " + this.position + "\n" + levelLayout + "\nMessage: " + this.message + "\n";
  }


  private static class LevelObject {

    private final String type;
    private final Location position;

    private LevelObject(String type, Location position) {
      this.type = type;
      this.position = position;
    }
  }

  private static class ActorPosition {

    private final String type;
    private final Location position;

    public ActorPosition(String type, Location position) {
      this.type = type;
      this.position = position;
    }
  }
}
