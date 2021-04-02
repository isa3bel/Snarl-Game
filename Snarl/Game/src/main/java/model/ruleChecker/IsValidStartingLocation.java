package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.*;

import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Is the given space a valid starting location?
 */
public class IsValidStartingLocation implements InteractableVisitor<Void>, BiPredicate<Space, Location> {

  private Location exitLocation;
  private Location keyLocation;
  private final ArrayList<Location> adversaryLocations;

  public IsValidStartingLocation() {
    this.adversaryLocations = new ArrayList<>();
  }

  /**
   * Is this space a valid starting location?
   * @param space the space object
   * @param location the location of this space
   * @return if the space is a traversable space in a room and it is not the same as this.exitLocation
   *    or this.keyLocation
   */
  @Override
  public boolean test(Space space, Location location) {
    return space.acceptVisitor(new IsValidStartingSpace())
        && !location.equals(exitLocation)
        && !location.equals(keyLocation)
        && !this.adversaryLocations.contains(location);
  }

  @Override
  public Void visitKey(Key key) {
    this.keyLocation = key.getCurrentLocation();
    return null;
  }

  @Override
  public Void visitPlayer(Player player) {
    return null;
  }

  @Override
  public Void visitAdversary(Adversary adversary) {
    this.adversaryLocations.add(adversary.getCurrentLocation());
    return null;
  }

  @Override
  public Void visitExit(Exit exit) {
    this.exitLocation = exit.getCurrentLocation();
    return null;
  }

  @Override
  public Void visitWall(Wall wall) {
    return null;
  }

  /**
   * Is the type of the given space a valid starting position?
   */
  private static class IsValidStartingSpace implements SpaceVisitor<Boolean> {
    @Override
    public Boolean visitDoor(Door door) {
      // DECISION: doors are not valid starting locations
      return false;
    }

    @Override
    public Boolean visitAnyWall(Wall wall) {
      return false;
    }

    @Override
    public Boolean visitTile(Tile tile) {
      return true;
    }

    @Override
    public Boolean visitHallwayTile(HallwayTile tile) {
      return false;
    }
  }
}
