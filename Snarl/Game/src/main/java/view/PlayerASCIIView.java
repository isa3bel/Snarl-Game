package view;

import model.characters.Character;
import model.characters.Player;
import model.level.Level;
import model.level.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A view of the game state from the perspective of a player.
 */
public class PlayerASCIIView extends PlayerView {

  public PlayerASCIIView(Character character) {
    super(character, new ASCIISpace());
  }

  @Override
  public void renderLevel(Level level) {
    this.addTilesToRender(level);
    // DECISION: if an adversary and a key are at the same location, the key shows over the adversary
    level.interact(new PlayerASCIIInteraction(this.render, this.playerLocation, this::shouldBeInView,
        VIEW_DISTANCE), this.playerLocation);
  }

  @Override
  public void placePlayers(List<Player> players) {
    players.forEach(player -> {
      Location location = player.getCurrentLocation();

      if (this.shouldBeInView(location)) {
        player.acceptVisitor(new PlayerASCIIInteraction(this.render, this.playerLocation,
            this::shouldBeInView, VIEW_DISTANCE));
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
