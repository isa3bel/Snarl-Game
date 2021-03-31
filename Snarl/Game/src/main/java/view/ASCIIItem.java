package view;

import java.util.function.Function;

import model.item.Exit;
import model.item.Item;
import model.item.ItemVisitor;
import model.item.Key;

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

  @Override
  public String visitExit(Exit exit) {
    return "E";
  }
}
