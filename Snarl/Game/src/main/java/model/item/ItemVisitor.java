package model.item;

public interface ItemVisitor<T> {

  T visitKey(Key key);
}
