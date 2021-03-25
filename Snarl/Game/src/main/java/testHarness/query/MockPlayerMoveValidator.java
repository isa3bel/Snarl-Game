package testHarness.query;

import model.characters.Character;
import model.level.Level;
import model.level.Location;
import model.ruleChecker.PlayerMoveValidator;

import java.util.List;

public class MockPlayerMoveValidator extends PlayerMoveValidator {

  private final StringBuilder testOutput;

  MockPlayerMoveValidator(MockPlayer mockPlayer, Location nextLocation, StringBuilder testOutput) {
    super(mockPlayer, nextLocation);
    this.testOutput = testOutput;
  }

  @Override
  public boolean isValid(Level level, List<Character> characters) {
    boolean isValid = super.isValid(level, characters);

    if (!isValid) {
      this.testOutput
          .append("[ ")
          .append(this.character.getName())
          .append(", ")
          .append(this.nextMove.toString())
          .append(", \"Invalid\"]");
    }

    return isValid;
  }
}
