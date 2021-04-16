package model.ruleChecker;

public enum MoveResult {

  OK("Ok"), INVALID("Invalid"), EJECTED("Eject"), EXITED("Exit"), KEY("Key");

  private final String value;

  MoveResult(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  /**
   * Combines all the given move results.
   * @param a the first result to combine
   * @param b the second result to combine
   * @return the combined move result
   */
  public static MoveResult combine(MoveResult a, MoveResult b) {
    if (a == null) return b;
    if (b == null) return a;
    if (a.equals(INVALID) || b.equals(INVALID)) return INVALID;
    if (a.equals(EJECTED) || b.equals(EJECTED)) return EJECTED;
    if (a.equals(EXITED) || b.equals(EXITED)) return EXITED;
    if (a.equals(KEY) || b.equals(KEY)) return KEY;
    return OK;
  }
}
