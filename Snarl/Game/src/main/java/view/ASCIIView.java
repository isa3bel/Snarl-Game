package view;

import java.util.List;
import java.util.stream.Collectors;
import model.characters.Player;
import model.level.Level;

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
    ASCIIInteraction asciiInteraction = new ASCIIInteraction(this.render);
    level.interact(asciiInteraction, null);
  }

  /**
   * Places the characters in the 2D game representation.
   * @param players the players to place
   */
  public void placePlayers(List<Player> players) {
    ASCIIInteraction asciiInteraction = new ASCIIInteraction(this.render);
    players.forEach(player -> player.acceptVisitor(asciiInteraction));
  }

  /**
   * Outputs the drawn view to System.out.
   */
  @Override
  public void draw() {
    System.out.print(this.toString());
  }

  @Override
  public String toString() {
    String outputString = render.stream()
        .map(row -> String.join("", row))
        .collect(Collectors.joining("\n"));
    return outputString + "\n";
  }
}
