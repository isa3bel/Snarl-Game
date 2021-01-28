import static org.junit.Assert.*;

import org.junit.Test;

public class NumJsonNumberTest {

  @Test
  public void calculateTotalSum() {
    NumJsonNumber n = new NumJsonNumber(5);
    Integer i = n.calculateTotal(new Sum());
    assertEquals(i, (Integer)5);
  }

  @Test
  public void calculateTotalProduct() {
    NumJsonNumber n = new NumJsonNumber(5);
    Integer i = n.calculateTotal(new Product());
    assertEquals(i, (Integer)5);
  }

  @Test
  public void testToString() {
    NumJsonNumber n = new NumJsonNumber(5);
    String s = n.toString();
    assertEquals(s, "5");
  }
}