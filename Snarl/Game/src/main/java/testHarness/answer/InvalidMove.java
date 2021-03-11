package testHarness.answer;

public class InvalidMove extends StateAnswer {

  @Override
  public String toString() {
    return "[ \"Failure\", \"The destination position \", " + this.locationToString(this.queryLocation) +
        " , \" is invalid.\" ]";
  }
}
