package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Character;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Level;
import model.level.Location;
import model.level.SpaceVisitor;
import model.level.Wall;

import java.util.List;

/**
 * Validates the move of a character.
 */
public abstract class MoveValidator {

  protected Character character;
  protected Location nextMove;

  public MoveValidator(Character character, Location location) {
    this.character = character;
    this.nextMove = location;
  }

  /**
   * Moves the character to the location specified by nextMove.
   */
  public void executeMove() {
    this.character.moveTo(this.nextMove);
  }

  /**
   * Is the nextMove valid for this character?
   * @return if the move is valid
   */
  public abstract boolean isValid(Level level, List<Player> players);

  /**
   * Is the space a valid type for this move validator?
   * @param level the level that the space exists in
   * @param isTraversable the visitor that will determine if the space is valid
   * @return whether the space is traversable for this MoveValidator
   */
  protected boolean isTraversable(Level level, SpaceVisitor<Boolean> isTraversable) {
    // DECISION: automatically including exits here because right now all types can move onto exits
    IsExit isExit = new IsExit(this.nextMove);
    level.interact(isExit, this.nextMove);
    boolean isLocationAtExit = isExit.getIsLocationAtExit();

    return level.get(this.nextMove).acceptVisitor(isTraversable) || isLocationAtExit;
  }

  /**
   * Is the nextMove within the max distance that this validator allows?
   * @param moveDistance the max euclidian distance to the next move
   * @return whether the players location is within the given distance of the nextMove
   */
  protected boolean isValidDistance(int moveDistance) {
    return this.nextMove.euclidianDistance(this.character.getCurrentLocation()) <= moveDistance;
  }

  /**
   * Is the location in the given level already occupied by an adverary?
   * @param level the level to check in
   * @param location the location to check
   * @return if the level has an adversary at the given location
   */
  protected boolean isOccupiedByAdversary(Level level, Location location) {
    OccupiedByAdversary occupiedByAdversary = new OccupiedByAdversary(location);
    level.interact(occupiedByAdversary, location);
    return occupiedByAdversary.getIsOccupiedByAdversary();
  }

  /**
   * Are any of the items an exit at this location?
   */
  private static class IsExit implements InteractableVisitor<Void> {

    private Location location;
    private boolean isLocationAtExit;

    private IsExit(Location location) {
      this.location = location;
      this.isLocationAtExit = false;
    }

    boolean getIsLocationAtExit() {
      return this.isLocationAtExit;
    }

    @Override
    public Void visitKey(Key key) {
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
      this.isLocationAtExit = exit.getCurrentLocation().equals(this.location);
      return null;
    }

    @Override
    public Void visitWall(Wall wall) {
      return null;
    }
  }

  /**
   * Is there an adversary at this location?
   */
  private static class OccupiedByAdversary implements InteractableVisitor<Void> {

    private Location location;
    private boolean isOccupiedByAdversary;

    private OccupiedByAdversary(Location location) {
      this.location = location;
      this.isOccupiedByAdversary = false;
    }

    boolean getIsOccupiedByAdversary() {
      return this.isOccupiedByAdversary;
    }

    @Override
    public Void visitKey(Key key) {
      return null;
    }

    @Override
    public Void visitPlayer(Player player) {
      return null;
    }

    @Override
    public Void visitAdversary(Adversary adversary) {
      this.isOccupiedByAdversary = this.isOccupiedByAdversary
          || adversary.getCurrentLocation().equals(this.location);
      return null;
    }

    @Override
    public Void visitExit(Exit exit) {
      return null;
    }

    @Override
    public Void visitWall(Wall wall) {
      return null;
    }
  }
}
