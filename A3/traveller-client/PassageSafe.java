public class PassageSafe implements Command {

  private CharacterTown characterTown;

  PassageSafe(CharacterTown characterTown) {
    this.characterTown = characterTown;
  }

  public String toString() {
    return "safe: " + characterTown.toString();
  }

}
