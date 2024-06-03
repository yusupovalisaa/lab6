package server.commands;


import interlayer.reqyest.RemoveByIDRequest;
import interlayer.reqyest.Request;
import interlayer.response.RemoveByIdResponse;
import interlayer.response.Response;
import server.managers.CollectionManager;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 */
public class RemoveByID extends Command {
    private final CollectionManager collectionManager;

    public RemoveByID(CollectionManager collectionManager) {
        super("remove_by_id Id", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
    }


    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (RemoveByIDRequest) request;

        try {
            if (!collectionManager.checkExist(req.id)) {
                return new RemoveByIdResponse("Продукта с таким ID в коллекции нет!");
            }

            collectionManager.remove(req.id);
            collectionManager.save();
            return new RemoveByIdResponse(null);
        } catch (Exception e) {
            return new RemoveByIdResponse(e.toString());
        }
    }
}