package src;

public interface Command {

  /**
   * Execute the command on the given town network
   * @param visitor the visitor that is visiting this command
   * @throws IllegalStateException if the command is ill formed
   */
  void accept(Visitor visitor) throws IllegalStateException;

  /**
   * Visitor for the command interface
   */
  interface Visitor {
    void visitMakeTown(MakeTown makeTown);
    void visitPlaceCharacter(PlaceCharacter placeCharacter);
    void visitPassageSafe(PassageSafe passageSafe);
  }

}
