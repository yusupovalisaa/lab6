package server.network;

import client.Main;
import interlayer.reqyest.Request;
import interlayer.response.NoSuchCommandResponse;
import interlayer.response.Response;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import java.util.Arrays;

/**
 * Абстрактный класс для управления TCP-сервером.
 */
public abstract class TCPServer {
    private final InetSocketAddress addr;
    private final CommandHandler commandHandler;
    private Runnable afterHook;

    private final Logger logger = Main.logger;

    private boolean running = true;

    public TCPServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
    }

    /**
     * Получает адрес сервера.
     *
     * @return Адрес сервера.
     */
    public InetSocketAddress getAddr() {
        return addr;
    }

    /**
     * Получает данные с клиента.
     * Возвращает пару из данных и адреса клиента
     */
    public abstract Pair<byte[], SocketAddress> receiveData() throws IOException;

    /**
     * Отправляет данные клиенту
     */
    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    /**
     * Абстрактный метод для подключения к клиенту.
     *
     * @param addr Адрес клиента.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public abstract void connectToClient(SocketAddress addr) throws IOException;

    /**
     * Абстрактный метод для отключения от клиента.
     *
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public abstract void disconnectFromClient() throws IOException;


    /**
     * Абстрактный метод для закрытия серверного соединения.
     *
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public abstract void close() throws IOException;

    /**
     * Запускает сервер.
     *
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public void run() throws IOException {
        logger.info("Сервер запущен по адресу " + addr);

        while (running) {
            Pair<byte[], SocketAddress> dataPair;
            try {
                dataPair = receiveData();
            } catch (Exception e) {
                logger.error("Ошибка получения данных : " + e.toString(), e);
                disconnectFromClient();
                continue;
            }

            var dataFromClient = dataPair.getKey();
            var clientAddr = dataPair.getValue();

            Request request = null;
            try {
                logger.debug("Полученные данные: " + Arrays.toString(dataFromClient));
                request = SerializationUtils.deserialize(dataFromClient);
                logger.info("Обработка " + request + " из " + clientAddr);
            } catch (SerializationException e) {
                logger.error("Невозможно десериализовать объект запроса.", e);
                disconnectFromClient();
            }

            Response response = null;
            if (request != null) {
                try {
                    response = commandHandler.handle(request);
                    if (afterHook != null) afterHook.run();
                } catch (Exception e) {
                    logger.error("Ошибка выполнения команды : " + e.toString(), e);
                }
                if (response == null) response = new NoSuchCommandResponse(request.getName());

                var data = SerializationUtils.serialize(response);
                logger.info("Ответ: " + response);

                try {
                    sendData(data, clientAddr);
                    logger.info("Отправлен ответ клиенту " + clientAddr);
                } catch (Exception e) {
                    logger.error("Ошибка ввода-вывода : " + e.toString(), e);
                }
            }

            disconnectFromClient();
            logger.info("Отключение от клиента " + clientAddr);
        }

        try {
            close();
        } catch (IOException e) {
            logger.error("Ошибка при закрытии сервера : " + e.toString(), e);
        }
    }

    /**
     * Вызывает хук после каждого запроса.
     * @param afterHook хук, вызываемый после каждого запроса
     */
    public void setAfterHook(Runnable afterHook) {
        this.afterHook = afterHook;
    }

    /**
     * Останавливает сервер.
     */
    public void stop() {
        running = false;
    }
}
