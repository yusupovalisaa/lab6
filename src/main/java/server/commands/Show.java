package server.commands;


import interlayer.reqyest.Request;
import interlayer.response.Response;
import interlayer.response.ShowResponse;
import server.managers.CollectionManager;

/**
 * Команда 'show'. Выводит в стандартный поток вывода все элементы коллекции в строковом представлении.
 */
public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }


    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            return new ShowResponse(collectionManager.sortedBySize(), null);
        } catch (Exception e) {
            return new ShowResponse(null, e.toString());
        }
    }
}