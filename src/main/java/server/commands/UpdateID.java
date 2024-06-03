package server.commands;

import interlayer.reqyest.Request;
import interlayer.reqyest.UpdateRequest;
import interlayer.response.Response;
import interlayer.response.UpdateResponse;
import server.managers.CollectionManager;


/**
 * Команда 'update ID'. Обновляет элемент коллекции по ID.
 */
public class UpdateID extends Command{
    private final CollectionManager collectionManager;

    public UpdateID(CollectionManager collectionManager) {
        super("update Id", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }


    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (UpdateRequest) request;
        try {
            if (!collectionManager.checkExist(req.id)) {
                return new UpdateResponse("Продукта с таким ID в коллекции нет!");
            }
            if (!req.updatedMovie.validate()) {
                return new UpdateResponse( "Поля продукта не валидны! Продукт не обновлен!");
            }
            collectionManager.update(req.updatedMovie);
            collectionManager.save();
            return new UpdateResponse(null);
        } catch (Exception e) {
            return new UpdateResponse(e.toString());
        }
    }
}
