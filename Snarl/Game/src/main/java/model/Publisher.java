package model;

import java.util.ArrayList;
import java.util.List;
import view.ObserverView;
import view.View;

public class Publisher {
  List<Observer> observers;

  Publisher() {
    observers = new ArrayList<>();
  }

  // Adds the given observer to the list of observers
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  // sends a view of the updated game state to every observer
  public void update(GameManager gameManager) {
    View view = new ObserverView();
    gameManager.buildView(view);
    this.observers.forEach(observer ->  observer.update(view));
  }
}
