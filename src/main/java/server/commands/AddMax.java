package server.commands;


import interlayer.reqyest.AddMaxRequest;
import interlayer.response.AddMaxResponse;
import interlayer.response.AddResponse;
import interlayer.response.Response;
import interlayer.reqyest.AddRequest;
import interlayer.reqyest.Request;
import server.managers.CollectionManager;

/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.
 * Работает по полям oscarsCount.
 */
public class AddMax extends Command {
    private final CollectionManager collectionManager;

    public AddMax (CollectionManager collectionManager) {
        super("addMax", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (AddMaxRequest) request;
        try {
            if (!req.m.validate()) {
                return new AddMaxResponse(false, -1, "Поля фильма не валидны. Фильм не добавлен.");
            }
            var newId = collectionManager.getFreeId();
            if (req.m.getOscarsCount()> collectionManager.findMax()){
                req.m.setId(newId);
                collectionManager.add(req.m);
                collectionManager.save();
                return new AddMaxResponse(true, newId, null);
            }
            collectionManager.addLog("addMax" + req.m.getId(), true);
            return new AddMaxResponse(false, newId, null);
        }
        catch (Exception e){
            return new AddResponse(-1, e.toString());
        }
    }
}
