public class CharacterTown {

  private String character;
  private String town;

  CharacterTown(String character, String town) {
    this.character = character;
    this.town = town;
  }

  public String toString() {
    return this.character + ", " + this.town;
  }
}
