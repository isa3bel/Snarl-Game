package testHarness.query;

import model.characters.Player;
import model.level.Level;
import model.level.Location;
import model.ruleChecker.PlayerMoveValidator;

import java.util.ArrayList;
import java.util.List;

public class MockPlayerMoveValidator extends PlayerMoveValidator {

  private final ArrayList<String> testOutput;

  MockPlayerMoveValidator(MockPlayer mockPlayer, Location nextLocation, ArrayList<String> testOutput) {
    super(mockPlayer, nextLocation);
    this.testOutput = testOutput;
  }

  @Override
  public boolean isValid(Location currentLocation, Level level, List<Player> players) {
    boolean isValid = super.isValid(currentLocation, level, players);

    if (!isValid) {
      String invalidMove = "[ \"" + this.character.getName() + "\", { \"to\": " + this.nextMove.toString() +
          ", \"type\": \"move\" }, \"Invalid\"]";
      this.testOutput.add(invalidMove);
    }

    return isValid;
  }
}
