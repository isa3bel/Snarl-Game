package testHarness.answer;

import model.GameManager;
import view.JSONView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class Answer {

  private String rooms;
  private String hallways;
  private final GameManager gameManager;

  protected Answer(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  public void setRooms(String rooms) {
    this.rooms = rooms;
  }

  public void setHallways(String hallways) {
    this.hallways = hallways;
  }

  /**
   * Calculates string for the game state of the query.
   * @return the json string of game state
   */
  protected String getStateJson() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    JSONView view = new JSONView(this.rooms, this.hallways);
    this.gameManager.buildView(view);
    view.draw();
    return outputStream.toString();
  }

}
