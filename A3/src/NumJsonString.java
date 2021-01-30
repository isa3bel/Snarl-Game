public class NumJsonString implements NumJson {

  private String string;

  NumJsonString(String string) {
    this.string = string;
  }

  @Override
  public <T> T calculateTotal(Function<T> function) {
    return function.calculateString(this.string);
  }

  public String toString() {
    return "\"" + this.string + "\"";
  }
}
