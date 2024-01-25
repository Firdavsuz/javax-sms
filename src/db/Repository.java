package db;

import java.util.List;

public interface Repository<T> {
    void create(T t);
    List<T> findAll();
    void update(T t, Integer id);
    void delete(T t);
}
