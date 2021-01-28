import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runner for Warm-up Assignment 2
 */
public class A2 {

  /**
   *
   * @param args an array with input args to the program
   *             expecting [ filename, typeOfSum ]
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    // TODO: move parse input and accompanying stuff to a new class
    ArrayList<NumJson> parsedInput = A2.parseInput(System.in);
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
        // TODO: better error here
        throw new IOException("unexpected ...");
    }
  }

  private static ArrayList<NumJson> parseInput(InputStream input) throws IOException {
    Scanner scanner = new Scanner(input);
    ArrayList<NumJson> numJsons = new ArrayList<>();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(NumJson.class, new NumJsonDeserializer())
        .create();

    while (scanner.hasNext()) {
      String jsonString = A2.readJsonString(scanner, 0, 0, false);
      NumJson nextNumJson = gson.fromJson(jsonString, NumJson.class);
      numJsons.add(nextNumJson);
    }

    return numJsons;
  }

  private static String readJsonString(Scanner scanner, int arrBalance, int objBalance, boolean isInString)
      throws IOException {
    String next = scanner.next();
    int nextArrBalance = arrBalance;
    int nextObjBalance = objBalance;
    boolean nextIsInString = isInString;
    for (int idx = 0; idx < next.length(); idx++) {
      switch (next.charAt(idx)) {
        case '"':
          nextIsInString = !nextIsInString;
          break;
        case '[':
          nextArrBalance += nextIsInString ? 0 : 1;
          break;
        case ']':
          nextArrBalance += nextIsInString ? 0 : -1;
          break;
        case '{':
          nextObjBalance += nextIsInString ? 0 : 1;
          break;
        case '}':
          nextObjBalance += nextIsInString ? 0 : -1;
          break;
        default:
          continue;
      }
    }

    boolean isJsonComplete = nextArrBalance == 0 && nextObjBalance == 0 && !nextIsInString;
    if (!isJsonComplete && !scanner.hasNext()) {
      throw new IOException("invalid json - expected closing bracket for array or object.");
    }
    return isJsonComplete
        ? next
        : next + A2.readJsonString(scanner, nextArrBalance, nextObjBalance, nextIsInString);
  }
}
