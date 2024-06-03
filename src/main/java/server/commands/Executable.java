package server.commands;

import interlayer.response.Response;
import interlayer.reqyest.Request;

/**
 * Выполнить что-либо.
 */
public interface Executable {
    /**
     * Выполняет команду
     * @return true.
     */
    Response apply(Request request);
}
