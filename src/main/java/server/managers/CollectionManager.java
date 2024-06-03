package server.managers;

import interlayer.models.Movie;
import interlayer.models.MovieGenre;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import interlayer.utilities.MovieComparator;
import server.Main;
import server.managers.WithCsvManager;
import org.apache.logging.log4j.Logger;

/**
 * Работа с коллекцией.
 */
public class CollectionManager {
    private int currentId = 1;
    private Map<Long, Movie> movies = new HashMap<>();
    private ArrayList<Movie> collection = new ArrayList<Movie>();
    private ArrayDeque<String> logStack = new ArrayDeque<String>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final WithCsvManager withCsvManager;

    public CollectionManager(WithCsvManager withCsvManager) {
        this.lastInitTime = lastInitTime;
        this.lastSaveTime = lastSaveTime;
        this.withCsvManager = withCsvManager;
    }

    /**
     * @return последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекции.
     */
    public ArrayList<Movie> getCollection() {
        return collection;
    }

    /**
     * Сохраняет коллекцию в файл
     */
    public void save() {
        withCsvManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Получает элемент по ID.
     *
     * @param id Movie
     * @return movie по id
     */
    public Movie byId(long id) {
        return movies.get(id);
    }

    /**
     * Проверяет содержит ли коллекция Movie.
     * @param m Movie
     * @return Movie m
     */
    public boolean isСontain(Movie m) {
        return m == null || byId((int) m.getId()) != null;
    }

    /**
     * Получает свободный ID.
     *
     * @return id
     */
    public int getFreeId() {
        while (byId(++currentId) != null) ;
        return currentId;
    }


    /**
     * Добавляет Movie.
     *
     * @param m Movie
     * @return true
     */
    public boolean add(Movie m) {
        if (isСontain(m)) return false;
        movies.put(m.getId(), m);
        collection.add(m);
        update();
        return true;
    }

    /**
     * @param id ID элемента.
     * @return Проверяет, существует ли элемент с таким ID.
     */
    public boolean checkExist(long id) {
        return byId(id) != null;
    }

    /**
     * Обновляет Movie.
     *
     * @param m Movie
     * @return true
     */
    public boolean update(Movie m) {
        if (!isСontain(m)) return false;
        collection.remove(byId((int) m.getId()));
        movies.put(m.getId(), m);
        collection.add(m);
        update();
        return true;
    }

    /**
     * Удаляет Movie по ID.
     *
     * @param id Movie
     * @return true
     */
    public boolean remove(long id) {
        var m = byId((int) id);
        if (m == null) return false;
        movies.remove(m.getId());
        collection.remove(m);
        update();
        return true;
    }

    /**
     * Создает транзакцию или добавляет операцию в транзакцию.
     * @param cmd     String
     * @param isFirst boolean
     */
    public void addLog(String cmd, boolean isFirst) {
        if (isFirst)
            logStack.push("+");
        if (!cmd.equals(""))
            logStack.push(cmd);
    }

    /**
     * Фиксирует изменения коллекции.
     */
    public void update() {
        Collections.sort(collection);
    }


    public boolean validateAll() {
        List<Long> keys = new ArrayList<Long>(movies.keySet());
        for (int i = 0; i < keys.size(); i++) {
            Long key = keys.get(i);
            Movie m = movies.get(key);
            if (!m.validate()) {
                Main.logger.warn("Продукт с id=" + m.getId() + " имеет невалидные поля.");
                return false;
            }
            if (m.getDirector() != null) {
                if (!m.getDirector().validate()) {
                    Main.logger.warn("Производитель продукта с id=" + m.getId() + " имеет невалидные поля.");
                    return false;
                }
            }
        } ;
        Main.logger.info("! Загруженные продукты валидны.");
        return true;
        }


    /**
     * Загружает коллекцию из файла.
     * @return true, если загрузка прошла успешно, иначе false
     */
    public boolean loadCollection() {
        movies.clear();
        withCsvManager.readCollection(collection);
        lastInitTime = LocalDateTime.now();

        // Используем Stream API для обработки элементов коллекции
        boolean duplicateFound = collection.stream()
                .anyMatch(e -> byId((int) e.getId()) != null);

        if (duplicateFound) {
            collection.clear();
            return false;
        }

        // Обновляем текущий идентификатор и добавляем фильмы в карту
        collection.forEach(e -> {
            if (e.getId() > currentId) currentId = (int) e.getId();
            movies.put(e.getId(), e);
        });

        update();
        return true;
    }

    /**
     * Возвращает отсортированную коллекцию фильмов по размеру.
     * @return отсортированная коллекция фильмов
     */
    public List<Movie> sortedBySize() {
        return collection.stream()
                .sorted(Comparator.comparingInt(Movie::getSize))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает отсортированную коллекцию фильмов по ID.
     *
     * @return отсортированная коллекция фильмов
     */
    public List<Movie> sortedById() {
        return collection.stream()
                .sorted(Comparator.comparingLong(Movie::getId))
                .collect(Collectors.toList());
    }

        /**
         * Ищет максимальное значение по полю oscarsCount
         * @return maxOscars
         */
        public long findMax() {
            return collection.stream()
                    .mapToLong(Movie::getOscarsCount)
                    .max()
                    .orElse(0);
        }


    /**
     * Мешает коллекцию.
     */
    public List<Movie> shuffle() {
        collection.stream()
                .collect(Collectors.toList());
        if (collection.size() == 2) {
            Collections.swap(collection, 0, 1);
        } else {
            Collections.shuffle(collection);

        }
        return  collection;
    }

    /**
     * Очищает коллекцию, возвращая массив идентификаторов фильмов.
     *
     * @return массив ид фильмов
     */
    public long[] clear() {
        return collection.stream()
                .mapToLong(Movie::getId)
                .toArray();
    }


        /**
         * Выводит элементы, значение поля genre которых меньше заданного
         * @param genre MovieGenre
         * @return Array y, состоящий из ID
         */
        public long[] minGenre(MovieGenre genre) {
            return collection.stream()
                    .filter(m -> m.getGenre().compareTo(genre) < 0)
                    .mapToLong(Movie::getId)
                    .toArray();
        }


        /**
         * Сортирует поля oscarsCount
         * @return Array y, состоящий из отсортированных коллекций.
         */
        public long[] fieldOscars () {
            long x[] = new long[collection.size()];
            int count = 0;
            for (var mov : collection) {
                x[count] += mov.getOscarsCount();
                count += 1;
            }
            Arrays.sort(x);
            long y[] = new long[collection.size()];
            for (int i = collection.size() - 1; i >= 0; i--) {
                y[i] = x[collection.size() - i - 1];
            }
            return y;
        }

        @Override
        public String toString () {
            if (collection.isEmpty()) return "Коллекция пуста!";

            StringBuilder info = new StringBuilder();
            for (var m : collection) {
                info.append(m.toString() + "\n\n");
            }
            return info.toString().trim();
        }


    }