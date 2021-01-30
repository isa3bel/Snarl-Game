import static org.junit.Assert.*;
import org.junit.Test;

public class ResultTest {

  @Test
  public void testToStringNumber() {
    Result r = new Result(new NumJsonNumber(5), 5);
    String s = r.toString();
    assertEquals(s, "{ \"object\": 5, \"total\": 5 }");
  }

  @Test
  public void testToStringString() {
    Result r = new Result(new NumJsonString("test"), 0);
    String s = r.toString();
    assertEquals(s, "{ \"object\": \"test\", \"total\": 0 }");
  }

  @Test
  public void testToStringArray() {
    NumJson[] j = {new NumJsonNumber(6), new NumJsonNumber(5), new NumJsonString("5")};
    NumJsonArray arr = new NumJsonArray(j);
    Result r = new Result(arr, 11);
    String s = r.toString();

    assertEquals(s, "{ \"object\": [ 6, 5, \"5\" ], \"total\": 11 }");
  }

  @Test
  public void testToStringObject() {
    Function<Integer> product = new Product();
    Pair<String, NumJson> payloadPair = new Pair("payload", new NumJsonNumber(3));
    Pair<String, NumJson> payloadPairBad = new Pair("bad", new NumJsonNumber(3));
    Pair[] pairs = {payloadPair, payloadPairBad};
    NumJson obj = new NumJsonObject(pairs);

    Result r = new Result(obj, 3);
    String s = r.toString();
    assertEquals(s, "{ \"object\": { \"payload\": 3, \"bad\": 3 }, \"total\": 3 }");
  }
}