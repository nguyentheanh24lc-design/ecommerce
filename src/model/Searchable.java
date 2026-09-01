package model;

import java.util.List;

public interface Searchable<T> {
T findById(String id);

List<T> findByKeyword(String keyword);

}
