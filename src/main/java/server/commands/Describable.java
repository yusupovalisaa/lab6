package server.commands;

/**
 * Сведения о команде.
 */
public interface Describable {
    /**
     * Получить имя команды.
     * @return name
     */
    String getName();

    /**
     *  Получить описание команды.
     * @return description
     */
    String getDescription();
}
