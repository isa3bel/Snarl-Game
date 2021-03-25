package testHarness.query;

import model.GameManager;
import model.characters.Player;
import model.item.Item;
import model.level.Location;
import model.ruleChecker.PlayerMoveValidator;

/**
 * Player class to be used for the state query in the test harness.
 */
public class MockPlayer extends Player {

  private final StringBuilder testOutput;
  private String recentMoveString;

  public MockPlayer(Location location, int id, String name, MockPlayerController controller,
      StringBuilder testOutput) {
    super(location, id, name, controller);
    this.testOutput = testOutput;
  }

  @Override
  public PlayerMoveValidator getNextMove() {
    return new MockPlayerMoveValidator(this, this.controller.getNextMove(), this.testOutput);
  }

  @Override
  public void updateController(GameManager gameManager) {
    this.testOutput.append(this.recentMoveString);
    MockPlayerView view = new MockPlayerView(this);
    this.controller.update(view);
  }

  @Override
  public void defend() {
    super.defend();

    this.testOutput.append(this.getMoveResponse(this.getCurrentLocation(), "Eject"));
    this.recentMoveString = "";
  }

  public void addToInventory(Item item) {
    super.addToInventory(item);

    // TODO: right now, if a key is placed on top of the exit or adversary, this might add multiple
    //  responses to the test output for the same move - is this expected? (and how would that actually work)
    this.testOutput.append(this.getMoveResponse(this.getCurrentLocation(), "Key"));
    this.recentMoveString = "";
  }

  @Override
  public void moveTo(Location location) {
    super.moveTo(location);

    if (location == null) {
      this.testOutput.append(this.getMoveResponse(this.getCurrentLocation(), "Exit"));
      this.recentMoveString = "";
    }
    else {
      this.recentMoveString = this.getMoveResponse(location, "Ok");
    }
  }

  /**
   * Formats the string for the test output of a move response.
   * @param location the location of the move
   * @param result the result of the move
   * @return the formatted string
   */
  private String getMoveResponse(Location location, String result) {
    return "[ " + this.getName() + ", { \"type\": \"move\", \"to\": " + location.toString()
        + "}, \"" + result + "\"]";
  }

}
