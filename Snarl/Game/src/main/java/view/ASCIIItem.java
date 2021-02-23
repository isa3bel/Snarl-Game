package view;

import java.util.function.Function;
import model.Item;
import model.ItemVisitor;
import model.Key;

/**
 * Translates an Item to its ASCII representation.
 */
public class ASCIIItem implements ItemVisitor<String>, Function<Item, String> {

  @Override
  public String apply(Item item) {
    return item.acceptVisitor(this);
  }

  @Override
  public String visitKey(Key key) {
    return "K";
  }
}
