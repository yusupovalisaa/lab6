package server.managers;

import au.com.bytecode.opencsv.*;
import interlayer.models.Movie;
import interlayer.reqyest.Request;
import interlayer.response.Response;
import server.Main;

import java.io.*;
import java.util.*;

/**
 * Использует файл для сохранения и загрузки коллекции.
 */
public class WithCsvManager {
    private final String fileName;
    private final Properties properties = new Properties();


    public WithCsvManager(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Преобразует коллекцию в CSV-строку.
     * @param collection
     * @return CSV-строка
     */
    public String collectionToFile(Collection<Movie> collection) {
        try {
            StringWriter sw = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(sw, ';');
            for (var e : collection) {
                System.out.println(e);
                csvWriter.writeNext(Movie.toArray(e));
            }
            String csv = sw.toString();
            Main.logger.info("Коллекция успешно преобразована в csv-строку");
            return csv;
        } catch (Exception e) {
            Main.logger.info("Ошибка сериализации при преобразовании в csv-строку");
            return ("Ошибка сериализации");
        }
    }

    /**
     *     Записывает коллекцию в файл.
     */
    public void writeCollection(Collection<Movie> collection) {
        try {
            var csv = collectionToFile(collection);
            if (csv == null) return;
            String currentDir = System.getProperty("user.dir");
            FileOutputStream outputStream = new FileOutputStream(currentDir + '/' + this.fileName);
            BufferedOutputStream buffOutputStr = new BufferedOutputStream(outputStream);
            try {
                buffOutputStr.write(csv.getBytes());
                buffOutputStr.flush();
                buffOutputStr.close();
                Main.logger.info("Коллекция успешна сохранена в файл!");
            } catch (IOException e) {
                Main.logger.error("Неожиданная ошибка сохранения");
            }
        } catch (FileNotFoundException | NullPointerException e) {
            Main.logger.error("Файл не найден");
        }
    }

    /**
     * Преобразует CSV-строку в коллекцию.
     * @param CSV-строка
     * @return коллекция
     */
    private LinkedList<Movie> CSV2collection(String s) {
        try {
            StringReader sr = new StringReader(s);
            CSVReader csvReader = new CSVReader(sr, ';');
            LinkedList<Movie> ds = new LinkedList<Movie>();
            String[] record = null;
            while ((record = csvReader.readNext()) != null) {
                Movie m = Movie.fromArray(record);
                if (m.validate())
                    ds.add(m);
                else
                    Main.logger.info("Файл с колекцией содержит не действительные данные");
            }
            csvReader.close();
            return ds;
        } catch (Exception e) {
            Main.logger.info("Ошибка десериализации");
            return null;
        }
    }

    /**
     *     Считывает коллекцию из файла.
     */
    public void readCollection(Collection<Movie> collection) {
        if (this.fileName != null && !this.fileName.isEmpty()) {
            String currentDir = System.getProperty("user.dir");
            try (var fileReader = new Scanner(new File(currentDir + '/' +this.fileName))) {
                var s = new StringBuilder("");
                while (fileReader.hasNextLine()) {
                    s.append(fileReader.nextLine());
                    s.append("\n");
                }
                collection.clear();
                for (var e : CSV2collection(s.toString()))
                    collection.add(e);
                if (collection != null) {
                    Main.logger.info("Коллекция успешна загружена!");
                    return;
                } else
                    Main.logger.error("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (FileNotFoundException exception) {
                Main.logger.error("Загрузочный файл не найден! Был создан файл с введённым названием.");
            } catch (IllegalStateException exception) {
                Main.logger.error("Непредвиденная ошибка!");
                System.exit(0);
            } catch (NullPointerException exception){
                Main.logger.error("Загрузочный файл некорректный!");
            }
        } else {
            Main.logger.error("Аргумент командной строки с загрузочным файлом не найден!");
        }
        collection = new ArrayList<Movie>();
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

