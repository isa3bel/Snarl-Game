import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class InputReader<T> {

  private Scanner scanner;
  private Class<T> classOfT;
  private Gson gson;

  InputReader(InputStream input, Class<T> classOfT, JsonDeserializer<T> deserializer) {
    this.scanner = new Scanner(input);
    this.classOfT = classOfT;
    this.gson = new GsonBuilder()
        .registerTypeAdapter(classOfT, deserializer)
        .create();
  }

  public ArrayList<T> parseInput() throws IOException {
    ArrayList<T> commands = new ArrayList<>();

    while (scanner.hasNext()) {
      String jsonString = this.readJsonString(0, 0, false);
      T nextCommand= this.gson.fromJson(jsonString, this.classOfT);
      commands.add(nextCommand);
    }

    return commands;
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
