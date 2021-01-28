import com.google.gson.JsonParseException;

/**
 * A list of NumJson.
 */
public class NumJsonArray implements NumJson {

  private NumJson[] array;

  /**
   * Creates a NumJsonAray with the given array elements.
   * @param array the NumJson elements of the array
   * @throws JsonParseException if any array element is invalid NumJson (e.g. null)
   */
  NumJsonArray(NumJson[] array) throws JsonParseException {
    for (NumJson numJson : array) {
      if (numJson == null) {
        throw new JsonParseException("received invalid NumJson to NumJsonArray: null");
      }
    }

    this.array = array;
  }

  @Override
  public <T> T calculateTotal(Function<T> function) {
    return function.calculateArray(this.array);
  }

  @Override
  public String toString() {
    String contents = "";
    for (NumJson numJson : this.array) {
      contents += numJson.toString() + ", ";
    }
    return "[ " + contents + "]";
  }
}
