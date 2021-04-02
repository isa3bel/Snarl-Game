package view;

import java.util.ArrayList;
import java.util.function.Predicate;
import model.level.Location;
import model.ruleChecker.Interactable;

public class PlayerASCIIInteraction extends ASCIIInteraction {

  private Location playerLoc;
  Predicate<Location> shouldBeInView;
  private int viewDistance;

  PlayerASCIIInteraction(ArrayList<ArrayList<String>> render, Location playerLoc, Predicate<Location> shouldBeInView, int viewDistance) {
    super(render);
    this.playerLoc = playerLoc;
    this.shouldBeInView = shouldBeInView;
    this.viewDistance = viewDistance;
  }

  @Override
  protected void drawAt(Interactable interactable, String draw) {
    Location location = interactable.getCurrentLocation();
    int actualRow = location.getRow() - this.playerLoc.getRow() + this.viewDistance;
    int actualColumn = location.getColumn() - this.playerLoc.getColumn() + this.viewDistance;
    if (this.shouldBeInView.test(location)) {
      this.render.get(actualRow).set(actualColumn, draw);
    }
  }
}
