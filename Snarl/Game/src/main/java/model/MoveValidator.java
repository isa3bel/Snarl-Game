package model;

import java.util.ArrayList;

public abstract class MoveValidator<T extends Character> {

  protected T character;
  protected Location nextMove;

  MoveValidator(T character, Location location) {
    this.character = character;
    this.nextMove = location;
  }

  /**
   * Moves the character to the location specified by nextMove.
   */
  public void executeMove() {
    this.character.moveTo(this.nextMove);
  }

  /**
   * Is the nextMove valid for this character?
   * @return if the move is valid
   */
  public abstract boolean isValid(Level level, ArrayList<Character> characters);

}