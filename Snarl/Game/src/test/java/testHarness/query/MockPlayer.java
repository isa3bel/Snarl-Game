package testHarness.query;

import model.GameManager;
import model.characters.Player;
import model.level.Location;
import model.characters.MoveValidator;
import testHarness.answer.StateAnswer;
import testHarness.answer.Success;
import testHarness.answer.SuccessEjected;
import testHarness.answer.SuccessExited;

/**
 * Player class to be used for the state query in the test harness.
 */
public class MockPlayer extends Player {

  private InteractionResult result;
  private Location nextLocation;

  public MockPlayer(Location location, int id, String name) {
    super(location, id, name);
    this.result = InteractionResult.DEFAULT;
  }

  @Override
  public MoveValidator<MockPlayer> getNextMove() {
    return new MockPlayerMoveValidator(this, this.nextLocation);
  }

  @Override
  public void defend() {
    super.defend();
    this.result = InteractionResult.EJECTED;
  }

  @Override
  public void moveTo(Location location) {
    super.moveTo(location);
    this.result = location != null ? this.result : InteractionResult.EXITED;
  }

  /**
   * Sets the value of the next move for this character.
   * @param nextLocation the next location to move this character to
   */
  public void setNextLocation(Location nextLocation) {
    this.nextLocation = nextLocation;
  }

  /**
   * Constructs the result of the testing query for judging this mock player's interaction.
   * @param gameManager the gameManager of this interaction
   * @return the resulting interaction string as specified by milestone 5 tests
   */
  public StateAnswer getInteractionAnswer(GameManager gameManager) {
    switch (this.result) {
      case EJECTED:
        return new SuccessEjected(gameManager);
      case EXITED:
        return new SuccessExited(gameManager);
      default:
        return new Success(gameManager);
    }
  }

  private enum InteractionResult {
    EJECTED, EXITED, DEFAULT
  }
}
