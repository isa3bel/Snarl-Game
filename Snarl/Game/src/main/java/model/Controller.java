package model;

/**
 * The controller of a Character for a Snarl game - could be a library,
 * a connection to a user over TCP or STDIN, or something else.
 */
public interface Controller {

  /**
   * Queries whatever is controlling a Snarl character for the next move.
   * @return the requested next move
   */
  Location getNextMove();

}
