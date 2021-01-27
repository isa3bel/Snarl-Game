import javafx.util.Pair;

public abstract class Function<T> {

  public abstract T calculateNumber(Integer val);

  public abstract T calculateString(String val);

  public abstract T calculateArray(NumJson[] val);

  public abstract T calculateObject(Pair<String, NumJson>[] val);

}
