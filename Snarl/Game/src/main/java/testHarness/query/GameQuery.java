package testHarness.query;

import model.GameManager;
import model.ruleChecker.GameStatus;
import testHarness.answer.GameAnswer;
import testHarness.deserializer.GameQueryDeserializer;

import java.util.ArrayList;

public class GameQuery extends Question {

  private final int rounds;
  private final GameManager gameManager;
  private final ArrayList<String> testOutput;

  public GameQuery(int rounds, GameManager gameManager, ArrayList<String> testOutput) {
    this.rounds = rounds;
    this.gameManager = gameManager;
    this.testOutput = testOutput;
  }

  @Override
  public GameAnswer getAnswer() {
    int round = 0;
    GameStatus status = GameStatus.PLAYING;
    this.gameManager.updatePlayers();

    while (round < this.rounds && status == GameStatus.PLAYING) {
      try {
        status = this.gameManager.doRound();
      }
      catch (MockPlayerController.NoMovesLeft exception) {
        break;
      }
      round++;
    }

    return new GameAnswer(this.gameManager, String.join(", ", this.testOutput));
  }

  /**
   * Deserializes a GameQuery from the given string.
   * @param gameQuery the query to deserialize
   * @return the LevelQuery
   */
  public static GameAnswer deserialize(String gameQuery) {
    return Question.deserialize(gameQuery, new GameQueryDeserializer(), GameAnswer.class);
  }
}
