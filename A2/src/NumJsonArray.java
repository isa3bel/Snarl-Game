import com.google.gson.JsonParseException;
import javafx.util.Pair;

public class NumJsonArray implements NumJson {

  private NumJson[] array;

  NumJsonArray(NumJson[] array) {
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
