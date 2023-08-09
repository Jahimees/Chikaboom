package net.chikaboom.service.data;

import java.util.List;
import java.util.Optional;

public interface DataService<T> {

    Optional<T> findById(int id);

    List<T> findAll();

    void deleteById(int id);

    T update(T o);

    T create(T account);
}
