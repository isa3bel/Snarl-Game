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

  }

  @Test
  public void testToStringObject() {

  }
}