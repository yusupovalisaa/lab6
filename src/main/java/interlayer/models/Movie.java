package interlayer.models;
import interlayer.utilities.Elements;
import interlayer.utilities.Validatable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


/**
 * Класс Movie.
 */
public class Movie extends Elements implements Validatable, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private MovieGenre genre; //Поле не может быть null
    private MpaaRating mpaaRating; //Поле может быть null
    private Person director; //Поле не может быть null

    public Movie(long id, String name, Coordinates coordinates, LocalDateTime creationDate, Long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person director) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.director = director;
    }

    public Movie(long id, String name, Coordinates coordinates, Long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person director) {
        this(id, name, coordinates, LocalDateTime.now(), oscarsCount, genre, mpaaRating, director);
    }
    @Override
    public String toString() {
        return  "Movie{\"id\": \"" + id + "\", " +
                "\"name\": \"" + name + "\", " +
                "\"creationDate\": \"" + creationDate.format(DateTimeFormatter.ISO_DATE_TIME) + "\", " +
                "\"oscarsCount\": " + oscarsCount + ", " +
                "\"MovieGenre\": " + ("\""+genre+"\"") + ", " +
                "\"MpaaRating\": " + (mpaaRating == null ? "null" : "\""+mpaaRating+"\"") + ", " +
                "\"Director\": \"" + director + "}\"";
    }

    /**
     * Проверка на корректность полей класса Movie
     * @return true
     */
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (creationDate == null) return false;
        if (oscarsCount <= 0) return false;
        if (genre == null) return false;
        if (director == null) return false;
        else return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie m = (Movie) o;
        return Objects.equals(id, m.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, oscarsCount, genre, mpaaRating, director);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getOscarsCount() {
        return oscarsCount;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public Person getDirector() {
        return director;
    }

    public int getSize(){return (1 + name.length() + coordinates.toString().length() + oscarsCount.toString().length() + genre.toString().length() + mpaaRating.toString().length() + director.toString().length());}


    /**
     * Получает из массива.
     * @return Movie
     */
    public static Movie fromArray(String[] a) {
        long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
        String name; //Поле не может быть null, Строка не может быть пустой
        Coordinates coordinates; //Поле не может быть null
        LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
        Long oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
        MovieGenre genre; //Поле не может быть null
        MpaaRating mpaaRating; //Поле может быть null
        Person director; //Поле не может быть null
        try {
            try { id = Long.parseLong(a[0]); } catch (NumberFormatException e) { id = Long.parseLong(null); }
            name = a[1];
            coordinates = new Coordinates(a[2]);
            try {
                creationDate = LocalDateTime.parse(a[3], DateTimeFormatter.ISO_DATE_TIME);
            }
            catch (DateTimeParseException e) { creationDate = null; };
            try { oscarsCount = (a[4].equals("null") ? null : Long.parseLong(a[4])); } catch (NumberFormatException e) { oscarsCount = null; }
            try { genre = MovieGenre.valueOf(a[5]); } catch (NullPointerException | IllegalArgumentException  e) { genre = null; }
            try { mpaaRating = MpaaRating.valueOf(a[6]); } catch (NullPointerException | IllegalArgumentException  e) { mpaaRating = null; }
            director = (a[7].equals("null") ? null : new Person(a[7]));
            return new Movie((int)id, name, coordinates, creationDate, oscarsCount, genre, mpaaRating, director);
        } catch (ArrayIndexOutOfBoundsException e) {}
        return null;
    }



    /**
     * Подаёт в массив.
     */
    public static String[] toArray(Movie e) {
        var list = new ArrayList<String>();
        list.add(Long.toString(e.getId()));
        list.add(e.getName());
        list.add(e.getCoordinates().toString());
        list.add(e.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME));
        list.add(e.getOscarsCount().toString());
        list.add(e.getGenre().toString());
        list.add(e.getMpaaRating() == null ? "null" : e.getMpaaRating().toString());
        list.add(e.getDirector().toString());
        return list.toArray(new String[0]);
    }


    @Override
    public int compareTo(Elements o) {
        return (int) (this.id - o.getId());
    }
}
