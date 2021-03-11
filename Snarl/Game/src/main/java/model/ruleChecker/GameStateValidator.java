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
   * @param characters the characters in this level
   * @return is this level configuration valid?
   */
  boolean isValid(Level level, List<Character> characters) {
    return characters.stream().allMatch(new IsLocationValid(level, characters));
  }

  /**
   * Should this game advance to the next level?
   * @param characters the characters that are in the game
   * @return whether the game should advace to the next level
   */
  public boolean shouldAdvanceLevel(List<Character> characters) {
    // TODO: does the level advance if _any_ player has reached the exit?
    //  what about if 1 player exited but others were ejected?
    return false;
  }

  /**
   * Evaluates the status of the current game.
   * @param currentLevel the current level in the game
   * @param levels the number of levels in the game
   * @param characters the characters in this game
   * @return the status of the game
   */
  public GameStatus evaluateGameState(int currentLevel, Level[] levels, List<Character> characters) {
    if (currentLevel == levels.length) return GameStatus.WON;
    if (characters.stream().allMatch(new EjectedPlayerOrEnemy())) return GameStatus.LOST;
    return GameStatus.PLAYING;
  }

  private static class IsLocationValid implements Predicate<Character>, CharacterVisitor<Boolean> {

    private Level level;
    private List<Character> characters;

    private IsLocationValid(Level level, List<Character> characters) {
      this.level = level;
      this.characters = characters;
    }

    @Override
    public boolean test(Character character) {
      return character.acceptVisitor(this);
    }

    @Override
    public Boolean visitPlayer(Player player) {
      return null;
    }

    @Override
    public Boolean visitAdversary(Adversary adversary) {
      return new AdversaryMoveValidator(adversary, adversary.getCurrentLocation()).isValid(this.level, this.characters);
    }
  }

  /**
   * Is the character an exited player or an adversary?
   */
  private static class EjectedPlayerOrEnemy implements Predicate<Character>, CharacterVisitor<Boolean> {
    @Override
    public Boolean visitPlayer(Player player) {
      return player.getCurrentLocation() == null;
    }

    @Override
    public Boolean visitAdversary(Adversary adversary) {
      return true;
    }

    @Override
    public boolean test(Character character) {
      return character.acceptVisitor(this);
    }
  }
}
