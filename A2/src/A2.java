import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class A2 {

  public static void main(String[] args) throws IOException {
    ArrayList<NumJson> parsedInput = A2.parseInput(System.in);
    Function<Integer> function = A2.getFunctionFromArg(args[1]);
    for (NumJson numJson : parsedInput) {
      int total = numJson.calculateTotal(function);
      Result result = new Result(numJson, total);

      // should be like "gson.serialize(result)" but i have not set that up yet
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
        throw new IOException("expected");
    }
  }

  private static ArrayList<NumJson> parseInput(InputStream input) throws IOException {
    Scanner scanner = new Scanner(input);
    ArrayList<NumJson> numJsons = new ArrayList<>();

    while (scanner.hasNext()) {
      String jsonString = A2.readJsonString(scanner, 0, 0, false);
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(NumJson.class, new NumJsonDeserializer())
          .create();

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
      char c = next.charAt(idx);
      boolean test = c == '[';
      switch (c) {
        case '"':
          nextIsInString = !nextIsInString;
          break;
        case '[':
          nextArrBalance += isInString ? 0 : 1;
          break;
        case ']':
          nextArrBalance += isInString ? 0 : -1;
          break;
        case '{':
          nextObjBalance += isInString ? 0 : 1;
          break;
        case '}':
          nextObjBalance += isInString ? 0 : -1;
          break;
        default: continue;
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
