package view;

import java.util.List;
import java.util.stream.Collectors;
import model.Character;
import model.Item;
import model.Level;
import model.Location;

import java.util.ArrayList;

/**
 * Creates the view for a Snarl game in with ASCII characters.
 */
public class ASCIIView implements View {

  ArrayList<ArrayList<String>> render;

  /**
   * Creates the initial representation of a level in the 2D string array.
   * @param level the level to render
   */
  public void renderLevel(Level level) {
    this.render = level.map(new ASCIISpace());
  }

  /**
   * Places the characters in the 2D game representation.
   * @param characters the characters to place
   */
  public void placeCharacters(List<Character> characters) {
    characters.forEach(character -> {
      Location location = character.getCurrentLocation();
      this.render.get(location.yCoordinate).set(location.xCoordinate, character.acceptVisitor(new ASCIICharacter()));
    });
  }

  /**
   * Places the items in the 2D game representation.
   * @param items the items to place
   */
  public void placeItems(List<Item> items) {
    items.forEach(item -> {
      Location location = item.getCurrentLocation();
      this.render.get(location.yCoordinate).set(location.xCoordinate, item.acceptVisitor(new ASCIIItem()));
    });
  }

  /**
   * Outputs the drawn view to System.out.
   */
  @Override
  public void draw() {
    final String outputString = render.stream()
        .map(row -> String.join("", row))
        .collect(Collectors.joining("\n"));
    System.out.print(outputString + "\n");
  }
}
