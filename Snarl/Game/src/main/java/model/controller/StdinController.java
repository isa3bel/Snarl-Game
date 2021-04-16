package model.controller;

import java.util.Scanner;

import model.characters.Character;
import model.level.Location;
import view.PlayerASCIIView;
import view.View;

/**
 * The controller that will control a character over standard in.
 */
public class StdinController implements Controller {

  private String name;

  public StdinController(String name) {
    this.name = name;
  }

  @Override
  public View getDefaultView(Character character) {
    return new PlayerASCIIView(character);
  }

  @Override
  public Location getNextMove() {
    while (true) {
      System.out.print(this.name + ", where would you like to go next? (int int)");
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
