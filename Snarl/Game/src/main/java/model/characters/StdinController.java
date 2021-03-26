package model.characters;

import java.util.Scanner;
import model.level.Location;
import view.View;

/**
 * The controller that will control a character over standard in.
 */
public class StdinController implements Controller {

  @Override
  public Location getNextMove() {
    while (true) {
      System.out.print("Where would you like to go next? (int int)");
      Scanner scanner = new Scanner(System.in);
      int row = scanner.nextInt();
      int col = scanner.nextInt();
      return new Location(row, col);
    }
  }

  @Override
  public void update(View view) {
    view.draw();
  }
}
