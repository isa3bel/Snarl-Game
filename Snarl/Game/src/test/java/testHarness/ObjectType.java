package testHarness;

import model.*;
import model.Character;

/**
 * Calculates the object type for a given location.
 */
public class ObjectType extends Interaction<Character> {

  private Location location;
  private String objectType;

  ObjectType(Location location) {
    super(null);
    this.location = location;
    this.objectType = null;
  }

  /**
   * Gets the object type calculated with this function object.
   * @return the object type that was calculated from interacting with the items
   */
  public String getObjectType() {
    return this.objectType;
  }

  @Override
  public void visitItem(Item item) {
    if (!item.getCurrentLocation().equals(this.location)) return;
    this.objectType = item.acceptVisitor(new KeyObjectType());
  }

  @Override
  public void visitExit(Exit exit) {
    this.objectType = "\"exit\"";
  }

  /**
   * Calculates the object type for a key.
   */
  private static class KeyObjectType implements ItemVisitor<String> {

    @Override
    public String visitKey(Key key) {
      return "\"key\"";
    }
  }
}
