package view;

import java.util.function.Function;
import model.characters.Adversary;
import model.characters.Character;
import model.characters.CharacterVisitor;
import model.characters.Player;

/**
 * Translates a Character to its ASCII representation.
 */
public class ASCIICharacter implements CharacterVisitor<String>, Function<Character, String> {

  @Override
  public String apply(Character character) {
    return character.acceptVisitor(this);
  }

  @Override
  public String visitPlayer(Player player) {
    return String.valueOf(player.id);
  }

  @Override
  public String visitAdversary(Adversary adversary) {
    return "A";
  }
}
