package testHarness;


import java.util.Scanner;

public class TestLevel {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNextLine()) {
      stringBuilder.append(scanner.nextLine());
    }
    Question nextUnit = LevelQuery.deserialize(stringBuilder.toString());
    System.out.print(nextUnit.getAnswer());
  }
}
