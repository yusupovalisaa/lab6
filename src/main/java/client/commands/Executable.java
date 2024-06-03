package client.commands;

/**
 * Выполнить что-либо.
 */
public interface Executable {
    /**
     * Выполняет команду
     * @return true.
     */
    boolean apply(String[] arguments);
}
