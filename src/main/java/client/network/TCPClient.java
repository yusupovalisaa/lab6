package client.network;

import client.Main;
import interlayer.reqyest.Request;
import interlayer.response.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/**
 * TCP-клиент для отправки запросов и получения ответов от сервера.
 */
public class TCPClient {
    private final int PACKET_SIZE = 2048;
    private final int DATA_SIZE = PACKET_SIZE - 1;

    private final InetSocketAddress addr;
    private final Logger logger = Main.logger;
    public TCPClient(InetAddress address, int port) {
        this.addr = new InetSocketAddress(address, port);
    }


    /**
     * Отправляет запрос на сервер и получает ответ.
     *
     * @param request Запрос, который нужно отправить.
     * @return Ответ от сервера.
     * @throws IOException            Если произошла ошибка ввода-вывода.
     * @throws ClassNotFoundException Если произошла ошибка при десериализации ответа.
     */
    public Response sendAndReceiveCommand(Request request) throws IOException {
        try (Socket clientSocket = new Socket()) {
            clientSocket.connect(addr);
            logger.info("Установлено соединение с сервером : " + addr);
            OutputStream outputStream = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream();
            logger.info("Отправка запроса на сервер : ");
            logger.info(clientSocket.getRemoteSocketAddress());
            byte[] data = serialize(request);

            logger.debug("Сериализованные данные: " + Arrays.toString(data));

            byte[][] chunks = new byte[(int) Math.ceil(data.length / (double) DATA_SIZE)][DATA_SIZE];

            int start = 0;
            for (int i = 0; i < chunks.length; i++) {
                chunks[i] = Arrays.copyOfRange(data, start, start + DATA_SIZE);
                start += DATA_SIZE;
            }

            for (int i = 0; i < chunks.length; i++) {
                byte[] chunk = chunks[i];
                if (i == chunks.length - 1) {
                    byte[] lastChunk = Arrays.copyOf(chunk, chunk.length + 1);
                    lastChunk[chunk.length] = 1;
                    outputStream.write(lastChunk);
                } else {
                    byte[] intermediateChunk = Arrays.copyOf(chunk, chunk.length + 1);
                    intermediateChunk[chunk.length] = 0;
                    outputStream.write(intermediateChunk);
                }
            }

            // Чтение данных от сервера
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[PACKET_SIZE];
            boolean received = false;

            logger.info("Ожидание ответа от сервера");

            while (!received) {
                int bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                }

                byteArrayOutputStream.write(buffer, 0, bytesRead);

                if (buffer[bytesRead - 1] == 1) {
                    received = true;
                }

                logger.debug("Прочитано " + bytesRead + " байт, полученные данные: " + Arrays.toString(Arrays.copyOf(buffer, bytesRead)));
            }

            byte[] responseBytes = byteArrayOutputStream.toByteArray();
            logger.info("Получен ответ от сервера: " + deserialize(responseBytes));
            return deserialize(responseBytes);
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка при десериализации ответа от сервера: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    /**
     * Сериализует запрос в массив байтов.
     *
     * @param request Запрос для сериализации.
     * @return Сериализованный запрос.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    private byte[] serialize(Request request) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(request);
            oos.flush();
            return baos.toByteArray();
        }
    }

    /**
     * Десериализует массив байтов в ответ.
     *
     * @param data Массив байтов для десериализации.
     * @return Десериализованный ответ.
     * @throws IOException            Если произошла ошибка ввода-вывода.
     * @throws ClassNotFoundException Если произошла ошибка при десериализации.
     */
    private Response deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Response) ois.readObject();
        }
    }
}
