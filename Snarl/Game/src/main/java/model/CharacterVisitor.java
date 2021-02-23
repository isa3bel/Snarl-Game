package model;

/**
 * Visitor for classes extending the Character abstract class.
 * @param <T> the return type of the visitor functions.
 */
public interface CharacterVisitor<T> {

  T visitPlayer(Player player);
  T visitAdversary(Adversary adversary);

}
