package src;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Reads input from an InputStream and transforms it to using Gson.
 * @param <T> the end result of the transformations
 */
public class InputReader<T> {

  private Scanner scanner;
  private Class<T> classOfT;
  private Gson gson;
  private Consumer<T> function;

  /**
   * Creates an InputReader class.
   * @param input the InputStream to parse from
   * @param classOfT the class to deserialize to
   * @param deserializer deserializer for the input - defaults to Gson default if null
   * @param function function to transform the input as it is parsed - defaults to do nothing if null
   * @throws IllegalArgumentException if input or classOfT are null
   */
  InputReader(InputStream input, Class<T> classOfT, JsonDeserializer<T> deserializer, Consumer<T> function)
    throws IllegalArgumentException {
    if (input == null) {
      throw new IllegalArgumentException("InputStream must not be null");
    }
    if (classOfT == null) {
      throw new IllegalArgumentException("Must specify the type of the class to deserialize");
    }

    this.scanner = new Scanner(input);
    this.classOfT = classOfT;
    this.gson = deserializer == null ?
        new Gson() :
        new GsonBuilder()
        .registerTypeAdapter(classOfT, deserializer)
        .create();
    this.function = function == null ? t -> {} : function;
  }

  /**
   * Parses input from the InputStream, transforms it, and calls function on the transformed input.
   * @return list of transformed inputs
   * @throws IOException if input closes
   */
  public ArrayList<T> parseInput() throws IOException {
    ArrayList<T> transformed = new ArrayList<>();

    while (scanner.hasNext()) {
      String jsonString = this.readJsonString(0, 0, false);
      T nextUnit = this.gson.fromJson(jsonString, this.classOfT);
      transformed.add(nextUnit);
      this.function.accept(nextUnit);
    }

    return transformed;
  }

  /**
   * Make sure that the input is reading an
   * @param arrBalance count of extra opening parens
   * @param objBalance count of extra closing parens
   * @param isInString is this currently reading inside a string?
   * @return the total json unit read from the input stream
   * @throws IOException if input closes
   */
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
