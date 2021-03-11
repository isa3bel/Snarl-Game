package testHarness.answer;

import model.GameManager;

public class SuccessEjected extends StateAnswer {

  private final GameManager gameManager;

  public SuccessEjected(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  public String toString() {
    return "[ \"Success\", \"Player \", \"" + this.playerName + "\", \" was ejected.\", " +
        this.getStateJson(this.gameManager) + " ]";
  }
}
