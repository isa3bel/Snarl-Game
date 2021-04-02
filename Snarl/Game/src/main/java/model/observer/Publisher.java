package model.observer;

import java.util.ArrayList;
import java.util.List;

import model.GameManager;
import view.ObserverView;
import view.View;

public class Publisher {
  List<Observer> observers;

  public Publisher() {
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
