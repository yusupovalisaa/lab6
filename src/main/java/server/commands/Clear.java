package server.commands;


import interlayer.response.ClearResponse;
import interlayer.response.Response;
import interlayer.reqyest.Request;
import server.managers.CollectionManager;

/**
 * Команда 'clear'. Очищает коллекцию.
 */

public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public Response apply(Request request){
        try {
            for (var ind : collectionManager.clear()) {
                collectionManager.remove(ind);
            }
            collectionManager.save();
            return new ClearResponse(null);
        } catch (Exception e) {
            return new ClearResponse(e.toString());
        }
    }
}
