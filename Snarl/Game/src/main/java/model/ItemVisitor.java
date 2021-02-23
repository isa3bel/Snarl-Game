package model;

public interface ItemVisitor<T> {

  T visitKey(Key key);
}
