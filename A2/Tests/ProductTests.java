import static org.junit.Assert.*;

import java.io.IOException;
import javafx.util.Pair;
import org.junit.Test;
import java.util.*;

public class ProductTests {

  @Test
  public void testCalculateInteger() throws IOException {
    Function p = new Product();
    Integer i = (Integer) p.calculateNumber(5);
    assertEquals(i, (Integer) 5);
  }

  @Test
  public void testCalculateString() throws IOException {
    Function p = new Product();
    Integer s = (Integer) p.calculateString("string");
    assertEquals(s, (Integer) 1);
  }

//  @Test
//  public void testCalculateArray() throws IOException {
//    Function p = new Product();
//   Pair<String, NumJson>[] pair = {new Pair<String, NumJson>("payload", new NumJsonNumber((Integer)5))};
//    NumJson[] arr = {new NumJsonNumber(5),
//        new NumJsonString((String)"test"),
//        new NumJsonObject(pair)};
//    Integer i = ((Product) p).calculateArray(arr);
//    assertEquals(i, (Integer)10);
//  }

//    @Test
//  public void  testCalculateObject() throws IOException {
//      Function p = new Product();
//      Pair<String, NumJson>[] pair = {new Pair<String, NumJson>("payload", new NumJsonNumber(5))};
//      Integer result = ((Product) p).calculateObject(pair);
//      assertEquals(result, (Integer) 5);
//  }


}