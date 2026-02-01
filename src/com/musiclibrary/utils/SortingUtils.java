package com.musiclibrary.utils;

import com.musiclibrary.model.Media;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SortingUtils {

    public static void sortByDuration(List<Media> mediaList) {
        mediaList.sort((m1, m2) -> Integer.compare(m1.getDuration(), m2.getDuration()));
    }

    public static void sortByTitle(List<Media> mediaList) {
        mediaList.sort(Comparator.comparing(Media::getTitle));
    }

    public static List<Media> filterMedia(List<Media> mediaList, Predicate<Media> predicate) {
        return mediaList.stream()
                .filter(predicate)
                .toList();
    }

    public static List<Media> filterByMinDuration(List<Media> mediaList, int minDuration) {
        return filterMedia(mediaList, media -> media.getDuration() >= minDuration);
    }

    public static void sortByArtistThenTitle(java.util.List<com.musiclibrary.model.Media> mediaList) {
    }
}