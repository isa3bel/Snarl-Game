package model;

import view.View;

/**
 * The GameManager for the Snarl game.
 */
public class GameManager {

  private final Level level;

  public GameManager(Level level) {
    this.level = level;
  }

  public void draw(View view) {
    view.draw(this.level);
  }
}
