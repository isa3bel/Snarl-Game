import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class A2 {

  public static void main(String[] args) throws IOException {
    ArrayList<JsonElement> parsedInput = A2.parseInput(System.in);

  }

  // ASSUMPTION: additional whitespace can be ignored, but there is always at least 1 whitespace
  private static ArrayList<JsonElement> parseInput(InputStream input) throws IOException {
    Scanner scanner = new Scanner(input);
    ArrayList<JsonElement> numJsons = new ArrayList<>();

    while (scanner.hasNext()) {
      /**
       * problem described here: https://piazza.com/class/kjj18tz29a76p2?cid=54
       * if we could have a delimeter this would be easier
       * or if we only got a single piece of json at a time, would also be easier
       *
       * ideally need to do something like:
       * https://stackoverflow.com/questions/20327670/deserialize-recursive-polymorphic-class-in-gson
       */
      String jsonString = A2.readJsonString(scanner, 0, 0);
      JsonElement parsed = JsonParser.parseString(jsonString);
      numJsons.add(parsed);
    }

    return numJsons;
  }

  private static String readJsonString(Scanner scanner, int arrBalance, int objBalance)
      throws IOException {
    if (arrBalance == 0 && objBalance == 0) return "";
    if (!scanner.hasNext()) {
      throw new IOException("invalid json - expected closing bracket for array or object.");
    }

    String next = scanner.next();

    // ASSUMPTION: no "[", "]", "{", or "}" in json strings
    if (next.contains("[")) arrBalance++;
    if (next.contains("{")) objBalance++;
    if (next.contains("]")) arrBalance--;
    if (next.contains("}")) objBalance--;

    return next + A2.readJsonString(scanner, arrBalance, objBalance);
  }
}
