package com.musiclibrary.repository.interfaces;

import java.util.List;

public interface SearchableRepository<T, ID> extends CrudRepository<T, ID> {
    List<T> findByTitle(String title);
    List<T> findByArtist(String artist);
    List<T> findByYear(int year);
    List<T> search(String keyword);
}
