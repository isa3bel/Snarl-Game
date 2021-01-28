import javafx.util.Pair;

/**
 * Function object that calculates the sum of all the numbers in a NumJson.
 */
public class Sum implements Function<Integer> {

  /**
   * Sum of a single number is just the number.
   * @param val the number to calculate sum of
   * @return the "sum" of that number
   */
  @Override
  public Integer calculateNumber(Integer val) {
    return val;
  }

  /**
   * Sum of a string is the identity - 0.
   * @param val the string
   * @return the sum identity, 0
   */
  @Override
  public Integer calculateString(String val) {
    return 0;
  }

  /**
   * Sum of this function applied to all NumJsonArray values
   * @param val the values of the array
   * @return the sum of the values in this array
   */
  @Override
  public Integer calculateArray(NumJson[] val) {
    Integer sum = 0;
    for (int idx = 0; idx < val.length; idx++) {
      sum += val[idx].calculateTotal(this);
    }
    return sum;
  }

  /**
   * Sum of the payload values of this NumJsonObject
   * @param val the key and value pairs of this object
   * @return the sum of the payload
   */
  @Override
  public Integer calculateObject(Pair<String, NumJson>[] val) {
    Integer sum = 0;
    for (Pair<String, NumJson> pair : val) {
      if (!pair.getKey().equals("payload")) continue;

      NumJson numJson = pair.getValue();
      sum += numJson.calculateTotal(this);
    }
    return sum;
  }
}
