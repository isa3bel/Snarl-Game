import static org.junit.Assert.*;

import org.junit.Test;

public class NumJsonArrayTest {

  @Test
  public void calculateTotalSum() {
    NumJson[] j = {new NumJsonNumber(5), new NumJsonString("5")};
    NumJsonArray arr = new NumJsonArray(j);
    Integer result = arr.calculateTotal(new Sum());
    assertEquals(result, (Integer) 5);
  }

  @Test
  public void calculateTotalProduct() {
    NumJson[] j = {new NumJsonNumber(6), new NumJsonNumber(5), new NumJsonString("5")};
    NumJsonArray arr = new NumJsonArray(j);
    Integer result = arr.calculateTotal(new Product());
    assertEquals(result, (Integer) 30);
  }

  @Test
  public void testToString() {
    NumJson[] j = {new NumJsonNumber(5), new NumJsonString("5")};
    NumJsonArray arr = new NumJsonArray(j);
    String s = arr.toString();
    assertEquals(s, "[ 5, \"5\" ]");
  }

}