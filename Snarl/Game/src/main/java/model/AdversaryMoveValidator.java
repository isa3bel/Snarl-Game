package model;

import java.util.ArrayList;

public class AdversaryMoveValidator extends MoveValidator<Adversary> {

  AdversaryMoveValidator(Adversary character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, ArrayList<Character> characters) {
    return true;
  }

}
