public class Result {

  private NumJson object;
  private int total;

  Result(NumJson object, int total) {
    this.object = object;
    this.total = total;
  }

  public String toString() {
    return "{ \"object\": " + this.object.toString() + ", \"total\": " + this.total + " }";
  }
}
