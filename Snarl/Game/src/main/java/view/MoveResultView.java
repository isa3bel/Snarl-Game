package view;

import model.characters.Player;
import model.level.Level;
import model.ruleChecker.MoveResult;

import java.util.List;

/**
 * View of a move result.
 */
public class MoveResultView implements View {

  private final MoveResult moveResult;

  public MoveResultView(MoveResult moveResult) {
    this.moveResult = moveResult;
  }

  @Override
  public void renderLevel(Level level) {
    // do not need to render level
  }

  @Override
  public void placePlayers(List<Player> players) {
    // do not need to place players
  }

  @Override
  public void draw() {
    System.out.println(this.toString());
  }

  public String toString() {
    return "\"" + this.moveResult.toString() + "\"";
  }
}
