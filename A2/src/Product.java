import com.google.gson.JsonParseException;
import javafx.util.Pair;

public class Product implements Function<Integer> {

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
    for (NumJson numJson : val) {
      product *= numJson.calculateTotal(this);
    }
    return product;
  }

  @Override
  public Integer calculateObject(Pair<String, NumJson>[] val) {
    Integer product = 1;
    for (Pair<String, NumJson> pair : val) {
      NumJson numJson = pair.getValue();
      if (pair.getKey().equals("payload")) {
        product += numJson.calculateTotal(this);
      }
    }
    return product;
  }
}
