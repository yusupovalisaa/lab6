package interlayer.models;

import java.io.Serializable;

/**
 * Жанры фильмов
 */
public enum MovieGenre implements Serializable {
    ACTION,
    WESTERN,
    COMEDY,
    TRAGEDY;

    /**
     * Получает строку со всеми элементами enum'а через запятую.
     * @return enum'ы
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var genres : values()) {
            nameList.append(genres.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}