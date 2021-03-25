package view;

import model.characters.Character;
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
  private final Location playerLocation;
  ArrayList<ArrayList<String>> render;

  public PlayerView(Player player) {
    this.playerLocation = player.getCurrentLocation();
    this.render = new ArrayList<>();

    for (int idx = 0; idx < 5; idx++) {
      this.render.add(new ArrayList<>(5));
    }
  }

  @Override
  public void renderLevel(Level level) {
    SpaceVisitor<String> asciiSpace = new ASCIISpace();
    level.filter((space, location) ->
      this.playerLocation.euclidianDistance(location) <= VIEW_DISTANCE
    ).forEach((location, space) ->
      this.render.get(location.getRow()).set(location.getColumn(), space.acceptVisitor(asciiSpace))
    );

    level.mapItems(item -> {
      Location location = item.getCurrentLocation();

      if (location.euclidianDistance(this.playerLocation) <= VIEW_DISTANCE) {
        this.render.get(location.getRow()).set(location.getColumn(), item.acceptVisitor(new ASCIIItem()));
      }
    });
  }

  @Override
  public void placeCharacters(List<Character> characters) {
    characters.forEach(character -> {
      Location location = character.getCurrentLocation();

      if (location.euclidianDistance(this.playerLocation) <= VIEW_DISTANCE) {
        this.render.get(location.getRow()).set(location.getColumn(), character.acceptVisitor(new ASCIICharacter()));
      }
    });
  }

  @Override
  public void draw() {
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
