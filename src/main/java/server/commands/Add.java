package server.commands;

import interlayer.response.AddResponse;
import interlayer.response.Response;
import interlayer.reqyest.AddRequest;
import interlayer.reqyest.Request;
import server.managers.CollectionManager;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command{
    private final CollectionManager collectionManager;


    public Add(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (AddRequest) request;
        try {
            if (!req.m.validate()) {
                return new AddResponse(-1, "Поля фильма не валидны. Фильм не добавлен.");
            }
            var newId = collectionManager.getFreeId();
            req.m.setId(newId);
            collectionManager.add(req.m);
            collectionManager.save();
            return new AddResponse(newId, null);
        }
        catch (Exception e){
            return new AddResponse(-1, e.toString());
        }
    }
}

