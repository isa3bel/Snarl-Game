package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Wall;

/**
 * An interaction for any character and something in the level.
 */
public abstract class Interaction implements InteractableVisitor<MoveResult> {

  @Override
  public MoveResult visitKey(Key key) {
    // nothing should happen
    return null;
  }

  @Override
  public MoveResult visitPlayer(Player player) {
    // nothing should happen
    return null;
  }

  @Override
  public MoveResult visitAdversary(Adversary adversary) {
    // nothing should happen
    return null;
  }

  @Override
  public MoveResult visitExit(Exit exit) {
    // nothing should happen
    return null;
  }

  @Override
  public MoveResult visitWall(Wall wall) {
    // nothing should happen
    return null;
  }

}
