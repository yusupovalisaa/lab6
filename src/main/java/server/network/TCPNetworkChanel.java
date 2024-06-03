package server.network;

import client.Main;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Класс для управления сетевыми соединениями по TCP с использованием канала.
 */
public class TCPNetworkChanel extends TCPServer {
    private final int PACKET_SIZE = 2048;
    private final int DATA_SIZE = PACKET_SIZE - 1;

    private final ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(PACKET_SIZE);

    private final Logger logger = Main.logger;

    public TCPNetworkChanel(InetAddress address, int port, CommandHandler commandHandler) throws IOException {
        super(new InetSocketAddress(address, port), commandHandler);
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(address, port));
    }

    /**
     * Получает данные от клиента.
     *
     * @return Пара байтовых данных и адреса клиента.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    public Pair<byte[], SocketAddress> receiveData() throws IOException {
        socketChannel = serverSocketChannel.accept();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SocketAddress addr = socketChannel.getRemoteAddress();

        try {
            logger.info("Соединено с " + addr);
        } catch (Exception e) {
            logger.error("Ошибка соединения с клиентом: " + e.toString(), e);
        }

        byteBuffer.clear();
        int bytesRead;
        while (true) {
            bytesRead = socketChannel.read(byteBuffer);

            if (bytesRead > 0) {
                byteBuffer.flip();
                byte[] data = new byte[byteBuffer.remaining()];
                byteBuffer.get(data);
                byteArrayOutputStream.write(data);
                byteBuffer.clear();

                logger.info("Получено \" + Arrays.toString(data) + \" от " + addr);

                // Проверка на окончание передачи данных
                if (data[data.length - 1] == 1) {
                    break;
                }
            } else if (bytesRead == 0) {
                // Неблокирующий режим - ждем данные
                continue;
            } else {
                // Соединение закрыто клиентом
                break;
            }
        }

        byte[] result = byteArrayOutputStream.toByteArray();
        return new ImmutablePair<>(result, addr);
    }


    /**
     * Отправляет данные клиенту.
     *
     * @param data Байтовые данные для отправки.
     * @param addr Адрес клиента.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    public void sendData(byte[] data, SocketAddress addr) throws IOException {
        connectToClient(addr);
        ByteBuffer sendBuffer = ByteBuffer.wrap(data);

        while (sendBuffer.hasRemaining()) {
            socketChannel.write(sendBuffer);
        }

        logger.info("Отправка данных завершена");
    }

    /**
     * Подключается к клиенту.
     *
     * @param addr Адрес клиента.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    public void connectToClient(SocketAddress addr) throws IOException {
        if (socketChannel == null || !socketChannel.isConnected()) {
            socketChannel.connect(addr);
        }
    }


    /**
     * Отключается от клиента.
     *
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    public void disconnectFromClient() throws IOException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }


    /**
     * Закрывает серверный канал.
     *
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    @Override
    public void close() throws IOException {
        serverSocketChannel.close();
    }
}
