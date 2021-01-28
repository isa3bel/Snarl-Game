/**
 * A NumJson with a method to accept visitors.
 */
public interface NumJson {

  /**
   * Accepts function object visitor.
   * @param function a function object to combine the values of this NumJson
   * @param <T> the type of the resulting "total"
   * @return the combined elemnts of this NumJson
   */
  <T> T calculateTotal(Function<T> function);
}
