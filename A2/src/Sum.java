import javafx.util.Pair;

public class Sum implements Function<Integer> {

  @Override
  public Integer calculateNumber(Integer val) {
    return val;
  }

  @Override
  public Integer calculateString(String val) {
    return 0;
  }

  @Override
  public Integer calculateArray(NumJson[] val) {
    Integer sum = 0;
    for (int idx = 0; idx < val.length; idx++) {
      sum += val[idx].calculateTotal(this);
    }
    return sum;
  }

  @Override
  public Integer calculateObject(Pair<String, NumJson>[] val) {
    Integer sum = 0;
    for (int idx = 0; idx < val.length; idx++) {
      Pair<String, NumJson> pair = val[idx];
      NumJson numJson = pair.getValue();
      if (pair.getKey().equals("payload")) {
        sum += numJson.calculateTotal(this);
      }
    }
    return sum;
  }
}
