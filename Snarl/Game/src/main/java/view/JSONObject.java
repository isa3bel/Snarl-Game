package view;

import model.*;
import model.Character;

public class JSONObject extends Interaction<Character> {

  private final Location location;
  private final StringBuilder objectString;
  private final StringBuilder isLocked;

  public JSONObject(Location location, StringBuilder objectString, StringBuilder isLocked) {
    super(null);
    this.location = location;
    this.objectString = objectString;
    this.isLocked = isLocked;
  }

  @Override
  public void visitItem(Item item) {
    if (!this.location.equals(item.getCurrentLocation())) return;

    String delimiter = this.objectString.length() == 0 ? "" : ",\n";
    String itemString = item.acceptVisitor(new KeyObjectType(this.location));
    this.objectString.append(delimiter).append(itemString);
  }

  @Override
  public void visitExit(Exit exit) {
    this.isLocked.append(exit.isLocked());

    String delimiter = this.objectString.length() == 0 ? "" : ",\n";
    this.objectString
        .append(delimiter)
        .append("{ \"type\": \"exit\", \"position\": [ ")
        .append(this.location.row)
        .append(", ")
        .append(this.location.column)
        .append(" ] }");
  }

  /**
   * Calculates the object type for a key.
   */
  private static class KeyObjectType implements ItemVisitor<String> {
    private final Location location;

    public KeyObjectType(Location location) {
      this.location = location;
    }

    @Override
    public String visitKey(Key key) {
      return "{ \"type\": \"key\", \"position\": [ " + this.location.row + ", " + this.location.column + " ] }";
    }
  }
}
