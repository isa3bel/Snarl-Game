import static org.junit.Assert.*;

import javafx.util.Pair;
import org.junit.Test;

public class SumTest {

  @Test
  public void calculateNumber() {
    Function<Integer> sum = new Sum();
    Integer result = sum.calculateNumber(5);
    assertEquals(result, new Integer(5));
  }

  @Test
  public void calculateString() {
    Function<Integer> sum = new Sum();
    Integer result = sum.calculateString("garbage");
    assertEquals(result, new Integer(0));
  }

  @Test
  public void calculateArray() {
    Function<Integer> sum = new Sum();
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5")};
    Integer result = sum.calculateArray(arr);
    assertEquals(result,new Integer(5));
  }

  @Test
  public void calculateArrayWithNestedArrayAndObject() {
    Function<Integer> sum = new Sum();
    Pair<String, NumJson> pair = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {pair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    Integer result = sum.calculateArray(nestingArr);
    assertEquals(result,new Integer(12));
  }

  @Test
  public void calculateObject() {
    Function<Integer> sum = new Sum();
    Pair<String, NumJson> payloadPair = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {payloadPair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    NumJsonArray numJsonArray2 = new NumJsonArray(nestingArr);
    Pair<String, NumJson> outerPayloadPair = new Pair("payload", numJsonArray2);
    Pair[] outerPairs = {outerPayloadPair};

    Integer result = sum.calculateObject(outerPairs);
    assertEquals(result,new Integer(12));
  }

  @Test
  public void calculateObjectDoesntAddNonPayload() {
    Function<Integer> sum = new Sum();
    Pair<String, NumJson> notPayloadPair = new Pair("badKey", new NumJsonNumber(3));
    Pair[] pairs = {notPayloadPair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    NumJsonArray numJsonArray2 = new NumJsonArray(nestingArr);
    Pair<String, NumJson> outerPayloadPair = new Pair("payload", numJsonArray2);
    Pair[] outerPairs = {outerPayloadPair};

    Integer result = sum.calculateObject(outerPairs);
    assertEquals(result,new Integer(9));
  }

  @Test
  public void calculateObjectDoesntAddNonPayloadChildren() {
    Function<Integer> sum = new Sum();
    Pair<String, NumJson> payloadPair = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {payloadPair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    NumJsonArray numJsonArray2 = new NumJsonArray(nestingArr);
    Pair<String, NumJson> notPayloadPair = new Pair("not payload", numJsonArray2);
    Pair<String, NumJson> outerPayloadPair = new Pair("payload", new NumJsonNumber(2));
    Pair[] outerPairs = {notPayloadPair, outerPayloadPair};

    Integer result = sum.calculateObject(outerPairs);
    assertEquals(result,new Integer(2));
  }
}