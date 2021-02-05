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
   */
  @Override
  public void accept(Command.Visitor visitor) {
    visitor.visitPlaceCharacter(this);
  }
}
