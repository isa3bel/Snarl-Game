package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Character;
import model.characters.CharacterVisitor;
import model.characters.Player;
import model.level.Level;

import java.util.List;
import java.util.function.Predicate;

/**
 * Validates the current game state.
 */
public class GameStateValidator {

  /**
   * Is the level configuration valid?
   * @param level the level of the game state
   * @param players the characters in this level
   * @return is this level configuration valid?
   */
  boolean isValid(Level level, List<Player> players) {
    // TODO: still needs to be fully implemented as we aren't exactly sure what this should be checking
    return true;
  }

  /**
   * Should this game advance to the next level?
   * @param players the characters that are in the game
   * @return whether the game should advance to the next level
   */
  public boolean shouldAdvanceLevel(List<Player> players) {
    // TODO: does the level advance if _any_ player has reached the exit?
    //  what about if 1 player exited but others were ejected?
    return false;
  }

  /**
   * Evaluates the status of the current game.
   * @param currentLevel the current level in the game
   * @param levels the number of levels in the game
   * @param players the players in this game
   * @return the status of the game
   */
  public GameStatus evaluateGameState(int currentLevel, Level[] levels, List<Player> players) {
    if (currentLevel == levels.length) return GameStatus.WON;
    if (players.stream().allMatch(player -> player.getCurrentLocation() == null)) return GameStatus.LOST;
    return GameStatus.PLAYING;
  }

}
