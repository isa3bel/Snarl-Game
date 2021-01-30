public class NumJsonNumber implements NumJson {

  private int number;

  NumJsonNumber(int number) {
    this.number = number;
  }

  @Override
  public <T> T calculateTotal(Function<T> function) {
    return function.calculateNumber(this.number);
  }

  @Override
  public String toString() {
    return "" + this.number;
  }
}
