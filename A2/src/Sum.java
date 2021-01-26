public class Sum implements Combiner<Integer> {

  public Integer combine(Integer a, Integer b) {
    return a + b;
  }
}
