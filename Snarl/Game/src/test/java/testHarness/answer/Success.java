package testHarness.answer;

import model.GameManager;

public class Success extends StateAnswer {

  private final GameManager gameManager;

  public Success(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  public String toString() {
    return "[ \"Success\", " + this.getStateJson(this.gameManager) + " ]";
  }
}
