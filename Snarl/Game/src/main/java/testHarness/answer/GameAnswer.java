package testHarness.answer;

import model.GameManager;

/**
 * The answer to a GameQuery.
 */
public class GameAnswer extends Answer {

  private final String managerTrace;

  public GameAnswer(GameManager gameManager, String managerTrace) {
    super(gameManager);
    this.managerTrace = managerTrace;
  }

  /**
   * This answer formatted as a string.
   * @return the result of a game query
   */
  @Override
  public String toString() {
    return "[ " + this.getStateJson() + ", [" + this.managerTrace + "] ]";
  }
}
