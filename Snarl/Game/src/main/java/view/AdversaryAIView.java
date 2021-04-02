package view;

import model.characters.Adversary;
import model.characters.Character;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.*;
import model.ruleChecker.InteractableVisitor;
import model.ruleChecker.MoveValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A view of the game state from the perspective of a player.
 */
public class AdversaryAIView implements View {

  private final Function<Location, MoveValidator> moveValidatorCreator;
  private final Location currentLocation;
  private Location[] validMoves;
  private Location nearestPlayerLocation;
  private Location keyLocation;
  private Location exitLocation;
  private Location closestWall;

  public AdversaryAIView(Location currentLocation, Function<Location, MoveValidator> moveValidatorCreator) {
    this.currentLocation = currentLocation;
    this.moveValidatorCreator = moveValidatorCreator;
  }

  @Override
  public void renderLevel(Level level) {
    this.validMoves = level.filter((space, location) -> {
      MoveValidator moveValidator = this.moveValidatorCreator.apply(location);
      return moveValidator.isValid(level, new ArrayList<>());
    }).keySet().toArray(new Location[0]);
    this.closestWall = level.filter(new IsWall()).keySet().stream()
        .min(Comparator.comparingInt(location -> location.euclidianDistance(this.currentLocation)))
        .orElse(null);
    level.interact(new LevelObjects(this), null);
  }

  @Override
  public void placePlayers(List<Player> players) {
    this.nearestPlayerLocation = players.stream()
        .map(Character::getCurrentLocation)
        .filter(Location::isInLevel)
        .min(Comparator.comparingInt(location -> location.euclidianDistance(this.currentLocation)))
        .orElse(null);
  }

  @Override
  public void draw() {
    System.out.println(this);
  }

  public String toString() {
    return "{\"validMoves\": " + Arrays.toString(validMoves) + ",\n" +
        "\"nearestPlayerLocation\": " + this.nearestPlayerLocation + ",\n" +
        "\"keyLocation\": " + this.keyLocation +",\n" +
        "\"exitLocation\": " + this.exitLocation +",\n" +
        "\"closestWall\": " + this.closestWall + "}";
  }

  /**
   * Is the given space a wall?
   */
  private static class IsWall implements BiPredicate<Space, Location>, SpaceVisitor<Boolean> {

    @Override
    public boolean test(Space space, Location location) {
      return space.acceptVisitor(this);
    }

    @Override
    public Boolean visitDoor(Door door) {
      return false;
    }

    @Override
    public Boolean visitAnyWall(Wall wall) {
      return true;
    }

    @Override
    public Boolean visitTile(Tile tile) {
      return false;
    }

    @Override
    public Boolean visitHallwayTile(HallwayTile tile) {
      return false;
    }
  }

  /**
   * Gets the key and exit Locations for this view.
   */
  private static class LevelObjects implements InteractableVisitor<Void> {

    private final AdversaryAIView view;

    LevelObjects(AdversaryAIView view) {
      this.view = view;
    }

    @Override
    public Void visitKey(Key key) {
      this.view.keyLocation = key.getCurrentLocation().isInLevel()
        ? key.getCurrentLocation() : null;
      return null;
    }

    @Override
    public Void visitPlayer(Player player) {
      return null;
    }

    @Override
    public Void visitAdversary(Adversary adversary) {
      return null;
    }

    @Override
    public Void visitExit(Exit exit) {
      this.view.exitLocation = exit.getCurrentLocation();
      return null;
    }

    @Override
    public Void visitWall(Wall wall) {
      return null;
    }
  }
}
