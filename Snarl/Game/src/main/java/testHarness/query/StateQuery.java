package testHarness.query;

import model.GameManager;
import testHarness.answer.InvalidMove;
import testHarness.answer.InvalidPlayer;
import testHarness.answer.StateAnswer;
import testHarness.deserializer.StateQueryDeserializer;

/**
 * A query about the neighbors to a location in a room.
 */
public class StateQuery extends Question {

  private final GameManager gameManager;
  private final MockPlayer player;

  public StateQuery(GameManager gameManager, MockPlayer player) {
    this.gameManager = gameManager;
    this.player = player;
  }

  /**
   * Deserializes a LevelQuery from the given string.
   * @param levelQuery the query to deserialize
   * @return the LevelQuery
   */
  public static StateAnswer deserialize(String levelQuery) {
    return Question.deserialize(levelQuery, new StateQueryDeserializer(), StateAnswer.class);
  }

  /**
   * Calculates the answer to this LocationQuery to be filled in with
   * String playerName, String roomsList, String hallwaysList
   * using MessageFormat in that order
   * @return the string representing the answer to this query
   */
  public StateAnswer getAnswer() {
    if (this.player == null) {
      return new InvalidPlayer();
    }
    if (!this.gameManager.isMoveValid(this.player.getNextMove())) {
      return new InvalidMove();
    }

//    ASCIIView view = new ASCIIView();
//    this.gameManager.buildView(view);
//    view.draw();

    this.gameManager.doTurn(this.player);
    return this.player.getInteractionAnswer(this.gameManager);
  }
}
