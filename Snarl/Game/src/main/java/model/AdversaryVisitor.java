package model;

/**
 * Visitor for classes extending the Adversary abstract class.
 * @param <T> the return type of the visitor functions.
 */
public interface AdversaryVisitor<T> {

  T visitGhost(Ghost ghost);
  T visitZombie(Zombie zombie);

}
