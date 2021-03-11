package testHarness.query;

import model.characters.Character;
import model.characters.*;
import model.level.Level;
import model.level.Location;
import model.ruleChecker.PlayerMoveValidator;

import java.util.ArrayList;

public class MockPlayerMoveValidator extends PlayerMoveValidator {

  MockPlayerMoveValidator(MockPlayer mockPlayer, Location nextLocation) {
    super(mockPlayer, nextLocation);
  }

  @Override
  public boolean isValid(Level level, ArrayList<Character> characters) {
    return level.get(this.nextMove).acceptVisitor(new IsTraversable()) &&
        characters.stream()
            .filter(c -> !c.equals(this.character))
            .filter(c -> c.acceptVisitor(new CanShareSpace()))
            .noneMatch(c -> c.getCurrentLocation().equals(this.nextMove));
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
