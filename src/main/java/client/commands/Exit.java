package client.commands;


import client.utilities.StandartConsole;

/**
 * Команда 'exit'. Завершает программу без сохранения в файл.
 */
public class Exit extends Command{
    private final StandartConsole console;

    public Exit(StandartConsole console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }


    /**
     * Выполняет команду
     * @return  успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        console.println("Завершение выполнения...".indent(1));
        return true;
    }
}
