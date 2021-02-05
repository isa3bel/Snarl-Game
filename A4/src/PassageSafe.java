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
   */
  @Override
  public void accept(Command.Visitor visitor) {
    visitor.visitPassageSafe(this);
  }
}
