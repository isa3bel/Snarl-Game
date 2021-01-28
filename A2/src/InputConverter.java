import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class InputConverter {

  private Scanner scanner;

  InputConverter(InputStream input) {
    this.scanner = new Scanner(input);
  }

  public ArrayList<NumJson> parseInput() throws IOException {
    ArrayList<NumJson> numJsons = new ArrayList<>();
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(NumJson.class, new NumJsonDeserializer())
        .create();

    while (scanner.hasNext()) {
      String jsonString = this.readJsonString(0, 0, false);
      NumJson nextNumJson = gson.fromJson(jsonString, NumJson.class);
      numJsons.add(nextNumJson);
    }

    return numJsons;
  }

  private String readJsonString(int arrBalance, int objBalance, boolean isInString)
      throws IOException {
    String next = this.scanner.next();
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
    if (!isJsonComplete && !this.scanner.hasNext()) {
      throw new IOException("invalid json - expected closing bracket for array or object.");
    }
    return isJsonComplete
        ? next
        : next + this.readJsonString(nextArrBalance, nextObjBalance, nextIsInString);
  }
}
