package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Level;
import model.level.Location;
import testHarness.query.IsTraversable;

import java.util.List;

/**
 * Validates the move of a player.
 */
public class PlayerMoveValidator extends MoveValidator {

  public PlayerMoveValidator(Player character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, List<Player> players) {
    boolean tileIsTraversable = level.get(this.nextMove).acceptVisitor(new IsTraversable());
    boolean noPlayersOnSpace = players.stream()
            .filter(c -> !c.equals(this.character))
            .noneMatch(c -> this.nextMove.equals(c.getCurrentLocation()));
    boolean isWithin2Squares = this.nextMove.euclidianDistance(this.character.getCurrentLocation()) <= 2;

    IsExit isExit = new IsExit(this.nextMove);
    level.interact(isExit);
    boolean isLocationAtExit = isExit.getIsLocationAtExit();

    return (tileIsTraversable || isLocationAtExit) && noPlayersOnSpace && isWithin2Squares;
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
      if (exit.getCurrentLocation().equals(this.location)) {
        this.isLocationAtExit = true;
      }
      return null;
    }
  }

}
