package interlayer.models;

import java.io.Serializable;

/**
 * Рейтинг фильмов
 */
public enum MpaaRating implements Serializable {
    G,
    PG,
    PG_13,
    NC_17;

    /**
     * Получает строку со всеми элементами enum'а через запятую.
     * @return enum'ы
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var mpaaRating : values()) {
            nameList.append(mpaaRating.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}

