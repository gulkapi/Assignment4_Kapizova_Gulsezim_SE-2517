package com.musiclibrary.interfaces;

import java.util.List;

public interface Searchable<T> {
    List<T> search(String keyword);
    default List<T> searchByTitle(String title) {
        return search("title:" + title);
    }
    static boolean isValidKeyword(String keyword) {
        return keyword != null && keyword.trim().length() >= 2;
    }
}