package src;

import com.google.gson.annotations.SerializedName;

/**
 * Command to place a character at a given town.
 */
public class PlaceCharacter implements Command {

  @SerializedName(value = "name", alternate = "character")
  private String character;
  private String town;

  /**
   * Visit this PlaceCharacter.
   * @param visitor visitor to act on this object
   * @throws IllegalStateException if character or town are null
   */
  @Override
  public void accept(Command.Visitor visitor) throws IllegalStateException {
    if (this.character == null) {
      throw new IllegalStateException("character must not be null");
    }
    if (this.town == null) {
      throw new IllegalStateException("town must not be null");
    }
    
    visitor.visitPlaceCharacter(this);
  }
}
