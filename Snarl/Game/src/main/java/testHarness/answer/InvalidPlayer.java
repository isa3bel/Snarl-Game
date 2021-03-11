package testHarness.answer;

public class InvalidPlayer extends StateAnswer {

  @Override
  public String toString() {
    return "[\"Failure\", \"Player, \", \"" + this.playerName + "\", \" is not part of the game.\"]";
  }

}
