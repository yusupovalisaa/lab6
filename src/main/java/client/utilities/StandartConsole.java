package client.utilities;

import client.utilities.Console;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Стандартная консоль для ввода команд и вывода результата.
 */
public class StandartConsole implements Console {
    private static final String P = "$ ";
    private static final Object P1 = " ";
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    /**
     * Вывод на одной строке.
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Вывод на новой строке.
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Вывод ошибок.
     */
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    /**
     * Вывод в две колонки.
     */
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }
    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).nextLine();
    }

    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

    public void selectFileScanner(Scanner scanner) {
        this.fileScanner = scanner;
    }

    public void selectConsoleScanner() {
        this.fileScanner = null;
    }

    public void prompt() {
        print(P1);
    }

    public String getPrompt() {
        return (String) P1;
    }
}

