import javafx.util.Pair;

public class Function<Integer> {

  private Integer total;
  protected Combiner<Integer> combiner;

  /**
   *  visitor for NumJson (ideally this would visit the JsonElement class
   *  but that class does not have methods built in to accept visitors)
   *  */

  void calculateNumber(Integer val) {
    this.total = this.combiner.combine(this.total, val);
  }

  void calculateString(String val) {
    // should not combine with total
  }

  void calculateArray(NumJson[] val) {
    for (int idx = 0; idx < val.length; idx++) {
      NumJson numJson = val[idx];
      numJson.calculateTotal(this);
    }
  }

  void calculateObject(Pair<String, NumJson>[] val) {
    for (int idx = 0; idx < val.length; idx++) {
      Pair<String, NumJson> pair = val[idx];
      String key = pair.getKey();

      if (key.equals("payload")) {
        NumJson payload = pair.getValue();
        payload.calculateTotal(this);
      }
    }
  }

}
