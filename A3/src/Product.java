
/**
 * Function object that calculates the product of all the numbers in a NumJson.
 */
public class Product implements Function<Integer> {

  /**
   * Product of a single number is just the number.
   * @param val the number to calculate product of
   * @return the "product" of that number
   */
  @Override
  public Integer calculateNumber(Integer val) {
    return val;
  }

  /**
   * Product of a string is the identity - 1.
   * @param val the string
   * @return the product identity, 1
   */
  @Override
  public Integer calculateString(String val) {
    return 1;
  }

  /**
   * Product of this function applied to all NumJsonArray values
   * @param val the values of the array
   * @return the product of the values in this array
   */
  @Override
  public Integer calculateArray(NumJson[] val) {
    Integer product = 1;
    for (NumJson numJson : val) {
      product *= numJson.calculateTotal(this);
    }
    return product;
  }

  /**
   * Product of the payload values of this NumJsonObject
   * @param val the key and value pairs of this object
   * @return the product of the payload
   */
  @Override
  public Integer calculateObject(Pair<String, NumJson>[] val) {
    Integer product = 1;
    for (Pair<String, NumJson> pair : val) {
      if (!pair.getKey().equals("payload")) continue;

      NumJson numJson = pair.getValue();
      product *= numJson.calculateTotal(this);

    }
    return product;
  }
}
