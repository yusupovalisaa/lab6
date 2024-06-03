package server.commands;

import interlayer.reqyest.Request;
import interlayer.response.PrintFieldDescendingOscarsCountResponse;
import interlayer.response.Response;
import server.managers.CollectionManager;

/**
 * Команда 'print_field_descending_oscars_count'.
 * Выводит значения поля oscarsCount всех элементов в порядке убывания.
 */
public class PrintFieldDescendingOscarsCount extends Command {
    private final CollectionManager collectionManager;

    public PrintFieldDescendingOscarsCount(CollectionManager collectionManager) {
        super("print_field_descending_oscars_count", "вывести значения поля oscarsCount всех элементов в порядке убывания");
        this.collectionManager = collectionManager;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try{
            return new PrintFieldDescendingOscarsCountResponse(collectionManager.fieldOscars(), null);
        }
        catch (Exception e){
            return new PrintFieldDescendingOscarsCountResponse(null, e.toString());
        }
    }
}
