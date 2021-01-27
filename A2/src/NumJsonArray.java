public class NumJsonArray implements NumJson {

  private NumJson[] array;

  NumJsonArray(NumJson[] array) {
    this.array = array;
  }

  @Override
  public <T> T calculateTotal(Function<T> function) {
    return function.calculateArray(this.array);
  }
}
