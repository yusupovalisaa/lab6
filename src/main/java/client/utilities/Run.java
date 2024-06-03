package client.utilities;

import client.Main;
import client.commands.*;
import client.network.TCPClient;
import client.utilities.Interrogator;
import client.utilities.StandartConsole;
import interlayer.exception.ScriptRecursionException;
import interlayer.utilities.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.apache.logging.log4j.Logger;


/**
 * Класс работы приложения.
 */
public class Run {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }

    private final StandartConsole console;
    private final TCPClient client;
    private final Map<String, Command> commands;

    private final Logger logger = Main.logger;
    private final List<String> scriptStack = new ArrayList<>();

    public Run(TCPClient client, StandartConsole console) {
        Interrogator.setUserScanner(new Scanner(System.in));
        this.client = client;
        this.console = console;
        this.commands = new HashMap<>() {{
            put(Commands.help, new Help(console, client));
            put(Commands.info, new Info(console, client));
            put(Commands.show, new Show(console, client));
            put(Commands.add, new Add(console, client));
            put(Commands.update, new UpdateID(console, client));
            put(Commands.remove_by_id, new RemoveByID(console, client));
            put(Commands.clear, new Clear(console, client));
            put(Commands.addMax, new AddMax(console, client));
            put(Commands.exit, new Exit(console));
            put(Commands.print_ascending, new PrintAscending(console, client));
            put(Commands.shuffle, new Shuffle(console, client));
            put(Commands.filter_than_less_genre, new FillterLessGenre(console, client));
            put(Commands.print_field_descending_oscars_count, new PrintFieldDescendingOscarsCount(console, client));
            put(Commands.execute_script, new ExecuteScript(console));
        }};
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        var userScanner = Interrogator.getUserScanner();
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};

            do {
                console.prompt();
                userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            console.printError("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        }
    }

    /**
     * Режим для запуска скрипта.
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(argument);
        if (!new File(argument).exists()) {
            argument = "../" + argument;
        }
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            Scanner tmpScanner = Interrogator.getUserScanner();
            Interrogator.setUserScanner(scriptScanner);
            Interrogator.setFileMode();

            do {
                userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (scriptScanner.hasNextLine() && userCommand[0].isEmpty()) {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getPrompt() + String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) throw new ScriptRecursionException();
                    }
                }
                commandStatus = launchCommand(userCommand);
            } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());

            Interrogator.setUserScanner(tmpScanner);
            Interrogator.setUserMode();

            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (ScriptRecursionException exception) {
            console.printError("Скрипты не могут вызываться рекурсивно!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * Запускает команду.
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    private ExitCode launchCommand(String[] userCommand) {
        if (userCommand[0].equals("")) return ExitCode.OK;
        var command = commands.get(userCommand[0]);
        if (command == null) {
            console.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!commands.get("exit").apply(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!commands.get("execute_script").apply(userCommand)) return ExitCode.ERROR;
                else return scriptMode(userCommand[1]);
            }
            default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
        };

        return ExitCode.OK;
    }
}