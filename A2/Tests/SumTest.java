import static org.junit.Assert.*;

import org.junit.Test;

public class SumTest {

  @Test
  public void calculateNumber() {
    Function p = new Sum();
    Integer i = (Integer) p.calculateNumber(5);
    assertEquals(i, (Integer) 5);
  }

  @Test
  public void calculateString() {
    Function p = new Sum();
    Integer i = (Integer) p.calculateNumber(5);
    assertEquals(i, (Integer) 0);
  }

  @Test
  public void calculateArray() {
    Function p = new Sum();
    NumJson[] arr = {new NumJsonNumber(5), new NumJsonString("5")};
    Integer i = (Integer) p.calculateArray(arr);
    assertEquals(arr,5);
  }

  @Test
  public void calculateObject() {
  }
}