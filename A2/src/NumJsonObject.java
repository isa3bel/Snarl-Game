import javafx.util.Pair;

public class NumJsonObject implements NumJson {

  private Pair<String, NumJson>[] pairs;

  NumJsonObject(Pair<String, NumJson>[] pairs) {
    this.pairs = pairs;
  }

  @Override
  public <T> T calculateTotal(Function<T> function) {
    return function.calculateObject(this.pairs);
  }
}
