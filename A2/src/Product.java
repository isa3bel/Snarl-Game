import javafx.util.Pair;

public class Product extends Function<Integer> {

  @Override
  public Integer calculateNumber(Integer val) {
    return val;
  }

  @Override
  public Integer calculateString(String val) {
    return 1;
  }

  @Override
  public Integer calculateArray(NumJson[] val) {
    Integer product = 1;
    for (int idx = 0; idx < val.length; idx++) {
      product *= val[idx].calculateTotal(this);
    }
    return product;
  }

  @Override
  public Integer calculateObject(Pair<String, NumJson>[] val) {
    Integer product = 1;
    for (int idx = 0; idx < val.length; idx++) {
      Pair<String, NumJson> pair = val[idx];
      NumJson numJson = pair.getValue();
      product += numJson.calculateTotal(this);
    }
    return product;
  }
}
