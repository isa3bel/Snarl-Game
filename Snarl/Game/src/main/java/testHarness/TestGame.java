package testHarness;

import testHarness.answer.Answer;
import testHarness.query.GameQuery;

import java.util.Scanner;

public class TestGame {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNextLine()) {
      stringBuilder.append(scanner.nextLine());
    }

    Answer answer = GameQuery.deserialize(stringBuilder.toString());
    System.out.print(answer.toString());
  }
}
