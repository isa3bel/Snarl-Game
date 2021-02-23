package model;

import java.util.ArrayList;
import view.View;

/**
 * The GameManager for the Snarl game.
 */
public class GameManager {

  private final Level level;
  private final ArrayList<Character> characters;
  private final ArrayList<Item> items;

  GameManager(Level level, ArrayList<Character> characters, ArrayList<Item> items) {
    this.level = level;
    this.characters = characters;
    this.items = items;
  }

  /**
   * Adds the contents of this builder to the view.
   * @param view the view to view this game through
   */
  public void buildView(View view) {
    view.renderLevel(this.level);
    view.placeCharacters(this.characters);
    view.placeItems(this.items);
  }
}
