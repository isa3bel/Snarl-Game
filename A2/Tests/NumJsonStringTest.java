import static org.junit.Assert.*;

import org.junit.Test;

public class NumJsonStringTest {

  @Test
  public void calculateTotalSum() {
    NumJsonString n = new NumJsonString("test");
    Integer i = n.calculateTotal(new Sum());
    assertEquals(i, (Integer)0);
  }

  @Test
  public void calculateTotalProduct() {
    NumJsonString n = new NumJsonString("test");
    Integer i = n.calculateTotal(new Product());
    assertEquals(i, (Integer)1);
  }

  @Test
  public void testToString() {
    NumJsonString n = new NumJsonString("test");
    String s = n.toString();
    assertEquals(s,"\"test\"");
  }
}