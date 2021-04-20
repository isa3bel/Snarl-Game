package testHarness.query;

import model.GameManager;
import model.characters.Player;
import model.item.Item;
import model.level.Location;
import model.ruleChecker.PlayerMoveValidator;
import view.PlayerASCIIView;

import java.util.ArrayList;

/**
 * Player class to be used for the state query in the test harness.
 */
public class MockPlayer extends Player {

  private final ArrayList<String> testOutput;

  public MockPlayer(Location location, String name, MockPlayerController controller,
                    ArrayList<String> testOutput) {
    super(location, name, controller);
    this.testOutput = testOutput;
  }

  @Override
  public PlayerMoveValidator getNextMove() {
    Location nextMove = this.controller.getNextMove();
    nextMove = nextMove == null ? this.currentLocation : nextMove;
    return new MockPlayerMoveValidator(this, nextMove, this.testOutput);
  }

  @Override
  public void updateController(GameManager gameManager) {
    // don't update this player if they are not currently playing
    if (this.currentLocation == null) return;

    PlayerASCIIView view = new PlayerASCIIView(this);
    gameManager.buildView(view);
    this.controller.update(view);
  }

  public void defend() {
    String keyResult = this.testOutput.remove(this.testOutput.size() - 1).replace("OK", "Eject");
    this.testOutput.add(keyResult);
//    super.defend();
  }

  public void addToInventory(Item item) {
    // TODO: right now, if a key is placed on top of the exit or adversary, this might add multiple
    //  responses to the test output for the same move - is this expected? (and how would that actually work)
    String keyResult = this.testOutput.remove(this.testOutput.size() - 1).replace("OK", "Key");
    this.testOutput.add(keyResult);
    super.addToInventory(item);
  }

  @Override
  public void moveTo(Location location) {
    if (location == null) {
      String keyResult = this.testOutput.remove(this.testOutput.size() - 1).replace("OK", "Exit");
      this.testOutput.add(keyResult);
    }
    else {
      boolean didPass = location.equals(this.getCurrentLocation());
      this.testOutput.add(this.getMoveResponse(didPass ? null : location));
    }

    super.moveTo(location);
  }

  /**
   * Formats the string for the test output of a move response.
   * @param location the location of the move
   * @return the formatted string
   */
  private String getMoveResponse(Location location) {
    return "[ \"" + this.getName() + "\", { \"type\": \"move\", \"to\": " +
        (location == null ? "null" : location.toString()) + " }, \"OK\"]";
  }

}
