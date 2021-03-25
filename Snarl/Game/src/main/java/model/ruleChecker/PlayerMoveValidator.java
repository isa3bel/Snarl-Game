package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Character;
import model.characters.CharacterVisitor;
import model.characters.Player;
import model.level.Level;
import model.level.Location;
import testHarness.query.IsTraversable;

import java.util.List;

public class PlayerMoveValidator extends MoveValidator {

  public PlayerMoveValidator(Player character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, List<Character> characters) {
    boolean tileIsTraversable = level.get(this.nextMove).acceptVisitor(new IsTraversable());
    boolean noPlayersOnSpace = characters.stream()
            .filter(c -> !c.equals(this.character))
            .filter(c -> c.acceptVisitor(new CanShareSpace()))
            .noneMatch(c -> c.getCurrentLocation().equals(this.nextMove));
    boolean isWithin2Squares = character.getCurrentLocation().euclidianDistance(this.nextMove) <= 2;

    return tileIsTraversable && noPlayersOnSpace && isWithin2Squares;
  }

  private static class CanShareSpace implements CharacterVisitor<Boolean> {

    @Override
    public Boolean visitPlayer(Player player) {
      return true;
    }

    @Override
    public Boolean visitAdversary(Adversary adversary) {
      return false;
    }
  }

}
