package testHarness.query;

import model.characters.Controller;
import model.level.Location;
import view.View;

/**
 * A controller for a MockPlayer for the GameQuery (Milestone 7).
 */
public class MockPlayerController implements Controller {

  private final Location[] locations;
  private int index;
  private StringBuilder testOutput;

  public MockPlayerController(Location[] locations) {
    this.locations = locations;
    this.index = 0;
  }

  /**
   * Set the output of this MockController to be the given StringBuilder.
   * @param testOutput where the MockController will write updates
   */
  public void setTestOutput(StringBuilder testOutput) {
    this.testOutput = testOutput;
  }

  /**
   * Gets the next move for this player from the list of locations and then increments the
   * index to point at the next move. Throws a NoMovesLeft error if there are no moves left.
   * @return the next move
   */
  @Override
  public Location getNextMove() {
    if (this.index > this.locations.length) {
      throw new NoMovesLeft();
    }

    return this.locations[this.index++];
  }

  /**
   * Add this player update to the test harness output.
   * @param view the view of the game
   */
  @Override
  public void update(View view) {
    this.testOutput.append(view.toString());
  }

  /**
   * When the controller runs out of moves from the test harness input.
   */
  public static class NoMovesLeft extends RuntimeException { }
}
