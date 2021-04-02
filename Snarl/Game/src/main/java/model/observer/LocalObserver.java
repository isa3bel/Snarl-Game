package model.observer;

import view.View;

public class LocalObserver implements Observer {

  @Override
  public void update(View view) {
    view.draw();
  }
}
