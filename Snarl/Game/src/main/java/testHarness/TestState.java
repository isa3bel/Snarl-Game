package testHarness;

import testHarness.answer.Answer;
import testHarness.query.StateQuery;

import java.util.Scanner;

public class TestState {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{ query: ");
    while (scanner.hasNextLine()) {
      stringBuilder.append(scanner.nextLine());
    }
    stringBuilder.append(" }");

    Answer answer = StateQuery.deserialize(stringBuilder.toString());
    System.out.print(answer.toString());
  }
}
