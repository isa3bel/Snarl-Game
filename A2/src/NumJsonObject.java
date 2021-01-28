import com.google.gson.JsonParseException;
import javafx.util.Pair;

/**
 * An object wih String keys and NumJson elements as the values for those keys.
 */
public class NumJsonObject implements NumJson {

  private Pair<String, NumJson>[] pairs;

  /**
   * Creates a NumJsonObject with the given key/value pairs
   * @param pairs the list of key/value pairs in the json object
   * @throws JsonParseException if any of the values in the pair is an invalid NumJson element (e.g. null)
   */
  NumJsonObject(Pair<String, NumJson>[] pairs) throws JsonParseException {
    for (Pair<String, NumJson> pair : pairs) {
      NumJson numJson = pair.getValue();
      if (numJson == null) {
        throw new JsonParseException("received invalid NumJson to NumJsonObject: null");
      }
    }

    this.pairs = pairs;
  }

  @Override
  public <T> T calculateTotal(Function<T> function) {
    return function.calculateObject(this.pairs);
  }

  @Override
  public String toString() {
    String contents = "";
    for (Pair<String, NumJson> pair : this.pairs) {
      NumJson numJson = pair.getValue();
      contents += "\"" + pair.getKey() + "\": " + numJson.toString() + ", ";
    }
    return "{ " + contents + "}";
  }
}
