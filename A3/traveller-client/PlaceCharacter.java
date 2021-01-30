public class PlaceCharacter implements Command {

  private CharacterTown characterTown;

  PlaceCharacter(CharacterTown characterTown) {
    this.characterTown = characterTown;
  }

  public String toString() {
    return "place: " + this.characterTown.toString();
  }
}
