package testHarness.query;

import model.characters.Character;
import model.controller.Controller;
import model.level.Location;
import view.View;

import java.util.ArrayList;

/**
 * A controller for a MockPlayer for the GameQuery (Milestone 7).
 */
public class MockPlayerController implements Controller {

  private final Location[] locations;
  private int index;
  private ArrayList<String> testOutput;

  public MockPlayerController(Location[] locations) {
    this.locations = locations;
    this.index = 0;
  }

  /**
   * Set the output of this MockController to be the given StringBuilder.
   * @param testOutput where the MockController will write updates
   */
  public void setTestOutput(ArrayList<String> testOutput) {
    this.testOutput = testOutput;
  }

  @Override
  public View getDefaultView(Character character) {
    return null;
  }

  /**
   * Gets the next move for this player from the list of locations and then increments the
   * index to point at the next move. Throws a NoMovesLeft error if there are no moves left.
   * @return the next move
   */
  @Override
  public Location getNextMove() {
    if (this.index >= this.locations.length) {
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
    this.testOutput.add(view.toString());
  }

  /**
   * When the controller runs out of moves from the test harness input.
   */
  public static class NoMovesLeft extends RuntimeException { }
}
