public interface Function<T> {

   T calculateNumber(Integer val);

   T calculateString(String val);

   T calculateArray(NumJson[] val);

   T calculateObject(Pair<String, NumJson>[] val);

}
