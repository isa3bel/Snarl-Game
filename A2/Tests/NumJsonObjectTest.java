import static org.junit.Assert.*;

import javafx.util.Pair;
import org.junit.Test;

public class NumJsonObjectTest {

  @Test
  public void calculateTotalSum() {
    Function<Integer> sum = new Sum();
    Pair<String, NumJson> payloadPair = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {payloadPair};
    NumJson obj = new NumJsonObject(pairs);

    Integer result = obj.calculateTotal(sum);
    assertEquals(result,new Integer(3));
  }

  @Test
  public void calculateTotalSumMore() {
    Function<Integer> product = new Product();
    Pair<String, NumJson> payloadPair = new Pair("payload", new NumJsonNumber(3));
    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage")};
    NumJsonArray numJsonArray2 = new NumJsonArray(nestingArr);
    Pair<String, NumJson> outerPayloadPair = new Pair("payload", numJsonArray2);
    Pair[] pairs = {payloadPair, outerPayloadPair};
    NumJson obj = new NumJsonObject(pairs);

    Integer result = obj.calculateTotal(product);
    assertEquals(result,new Integer(12));
  }

  @Test
  public void testToString() {
    Pair<String, NumJson> notPayloadPair = new Pair("badKey", new NumJsonNumber(3));
    Pair<String, NumJson> payload = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {notPayloadPair, payload};
    NumJson obj = new NumJsonObject(pairs);
    String s = obj.toString();
    assertEquals(s, "{ \"badKey\": 3, \"payload\": 3 }");
  }
}