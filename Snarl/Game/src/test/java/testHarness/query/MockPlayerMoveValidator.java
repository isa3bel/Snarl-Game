package testHarness.query;

import model.Character;
import model.Level;
import model.Location;
import model.MoveValidator;

import java.util.ArrayList;

public class MockPlayerMoveValidator extends MoveValidator<MockPlayer> {

  MockPlayerMoveValidator(MockPlayer mockPlayer, Location nextLocation) {
    super(mockPlayer, nextLocation);
  }

  @Override
  public boolean isValid(Level level, ArrayList<Character> characters) {
    return level.get(this.nextMove).acceptVisitor(new IsTraversable());
  }
}
