import static org.junit.Assert.*;

import java.io.IOException;
import javafx.util.Pair;
import org.junit.Test;
import java.util.*;

public class ProductTests {

  @Test
  public void testCalculateInteger() throws IOException {
    Function<Integer> product = new Product();
    Integer result = product.calculateNumber(5);
    assertEquals(result, (Integer) 5);
  }

  @Test
  public void testCalculateString() throws IOException {
    Function<Integer> product = new Product();
    Integer result = product.calculateString("string");
    assertEquals(result, (Integer) 1);
  }

  @Test
  public void calculateArray() {
    Function<Integer> product = new Product();
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5")};
    Integer result = product.calculateArray(arr);
    assertEquals(result,new Integer(5));
  }

  @Test
  public void calculateArrayWithNestedArrayAndObject() {
    Function<Integer> product = new Product();
    Pair<String, NumJson> pair = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {pair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    Integer result = product.calculateArray(nestingArr);
    assertEquals(result,new Integer(60));
  }

  @Test
  public void calculateObject() {
    Function<Integer> product = new Product();
    Pair<String, NumJson> payloadPair = new Pair("payload", new NumJsonNumber(3));
    Pair[] pairs = {payloadPair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    NumJsonArray numJsonArray2 = new NumJsonArray(nestingArr);
    Pair<String, NumJson> outerPayloadPair = new Pair("payload", numJsonArray2);
    Pair[] outerPairs = {outerPayloadPair};

    Integer result = product.calculateObject(outerPairs);
    assertEquals(result,new Integer(60));
  }

  @Test
  public void calculateObjectDoesntAddNonPayload() {
    Function<Integer> product = new Product();
    Pair<String, NumJson> notPayloadPair = new Pair("badKey", new NumJsonNumber(3));
    Pair[] pairs = {notPayloadPair};
    NumJson obj = new NumJsonObject(pairs);
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5"), obj};
    NumJsonArray numJsonArray = new NumJsonArray(arr);

    NumJson[] nestingArr = {new NumJsonNumber(4), new NumJsonString("garbage"), numJsonArray};
    NumJsonArray numJsonArray2 = new NumJsonArray(nestingArr);
    Pair<String, NumJson> outerPayloadPair = new Pair("payload", numJsonArray2);
    Pair[] outerPairs = {outerPayloadPair};

    Integer result = product.calculateObject(outerPairs);
    assertEquals(result,new Integer(20));
  }

  @Test
  public void calculateObjectDoesntAddNonPayloadChildren() {
    Function<Integer> product = new Product();
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

    Integer result = product.calculateObject(outerPairs);
    assertEquals(result,new Integer(2));
  }
}