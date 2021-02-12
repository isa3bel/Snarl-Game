package model;

import view.View;

public class GameManager {

  private Level level;

  public void draw(View view) {
    view.draw(this.level);
  }
}
