package src;

import com.google.gson.annotations.SerializedName;

/**
 * Executes the command to query if a passage is safe.
 */
public class PassageSafe implements Command {

  private String character;
  @SerializedName(value = "destination", alternate = "town")
  private String town;

  /**
   * Accept a visitor to this PassageSafe.
   * @param visitor the visitor to act on this PassageSafe
   * @throws IllegalStateException if this town or character does not exist
   */
  @Override
  public void accept(Command.Visitor visitor) throws IllegalStateException {
    if (this.character == null) {
      throw new IllegalStateException("character must not be null");
    }
    if (this.town == null) {
      throw new IllegalStateException("town must not be null");
    }

    visitor.visitPassageSafe(this);
  }
}
