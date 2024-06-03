package client.utilities;

import java.util.Scanner;

/**
 * Консоль для ввода команд и вывода результата.
 */
public interface Console {
    void print(Object o);
    void println(Object o);
    void printError(Object o);
    void printTable(Object o1, Object o2);
    void selectFileScanner(Scanner o);
    void selectConsoleScanner();
    void prompt();
}
