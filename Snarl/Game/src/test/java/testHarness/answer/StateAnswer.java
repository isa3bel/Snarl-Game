package testHarness.answer;

import model.GameManager;
import model.Location;
import view.JsonView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class StateAnswer extends Answer {

  protected String playerName;
  protected String rooms;
  protected String hallways;
  protected Location queryLocation;

  public StateAnswer setPlayerName(String playerName) {
    this.playerName = playerName;
    return this;
  }

  public StateAnswer setRooms(String rooms) {
    this.rooms = rooms;
    return this;
  }

  public StateAnswer setHallways(String hallways) {
    this.hallways = hallways;
    return this;
  }

  public StateAnswer setQueryLocation(Location location) {
    this.queryLocation = location;
    return this;
  }

  /**
   * Calculates string for the game state of the query.
   * @return the json string of game state
   */
  protected String getStateJson(GameManager gameManager) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    JsonView view = new JsonView(this.rooms, this.hallways);
    gameManager.buildView(view);
    return outputStream.toString();
  }

}
