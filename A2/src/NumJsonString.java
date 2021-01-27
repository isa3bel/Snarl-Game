public class NumJsonString implements NumJson {

  private String string;

  NumJsonString(String string) {
    this.string = string;
  }

  @Override
  public void calculateTotal(Function<Integer> function) {
    function.calculateString(this.string);
  }
}
