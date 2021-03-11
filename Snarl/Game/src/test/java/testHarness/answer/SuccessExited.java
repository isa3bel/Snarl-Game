package testHarness.answer;

import model.GameManager;

public class SuccessExited extends StateAnswer {

  private final GameManager gameManager;

  public SuccessExited(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  public String toString() {
    return "[ \"Success\", \"Player \", " + this.playerName + ", \" exited.\", " +
        this.getStateJson(this.gameManager) + " ]";
  }
}
