import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Runner for Warm-up Assignment 2
 */
public class A2 {

  /**
   * Takes the input, calculates the sum/product, and prints the result.
   * @param args an array with input args to the program
   *             expecting [ filename, typeOfSum ]
   * @throws IOException when System.in is garbage
   */
  public static void main(String[] args) throws IOException {
    InputConverter inputConverter = new InputConverter(System.in);
    ArrayList<NumJson> parsedInput = inputConverter.parseInput();
    Function<Integer> function = A2.getFunctionFromArg(args[0]);

    for (NumJson numJson : parsedInput) {
      if (numJson == null) {
        throw new JsonParseException("received invalid NumJson: null");
      }

      int total = numJson.calculateTotal(function);
      Result result = new Result(numJson, total);

      System.out.println(result.toString());
    }
  }

  private static Function<Integer> getFunctionFromArg(String functionType) throws IOException {
    switch (functionType) {
      case "--sum":
        return new Sum();
      case "--product":
        return new Product();
      default:
        throw new IOException("unexpected argument - expected '--sum' or '--product', got: " + functionType);
    }
  }
}
