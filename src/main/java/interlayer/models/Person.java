package interlayer.models;

import interlayer.utilities.Validatable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Класс Person.
 */
public class Person implements Validatable, Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private LocalDate birthday; //Поле может быть null
    private Color color; //Поле может быть null
    private Country nationality; //Поле может быть null

    public Person(String name, LocalDate birthday, Color color, Country nationality) {
        this.name = name;
        this.birthday = birthday;
        this.color = color;
        this.nationality = nationality;
    }
    public Person(String name, Color color, Country nationality){
        this(name, LocalDate.now(), color, nationality);
    }

    @Override
    public String toString() {
        return  "{\"name\": \"" + name + "\", " +
                "\"birthday\": " + (birthday == null ? "null": "\""+birthday.format(DateTimeFormatter.ISO_DATE)+"\"") + ", " +
                "\"color\": \"" + (color == null ? "null" : color) + "\", " +
                "\"nationality\": \"" + (nationality == null ? "null" : nationality) + "\"";
    }

    /**
     * Проверка на корректность полей класса Person
     * @return true
     */
    @Override
    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (birthday == null) return false;
        else return true;
    }

    public Person(String s) {
        try {
            this.name = s.split(", ")[0].substring(10, s.split(", ")[0].length() - 1);
            try { this.birthday = (s.split(", ")[1].substring(13, s.split(", ")[1].length() - 1)).equals("null") ? null : LocalDate.parse(s.split(", ")[1].substring(13, s.split(", ")[1].length() - 1), DateTimeFormatter.ISO_DATE); } catch (
                    DateTimeParseException e) { return; };
            try { this.color = Color.valueOf(s.split(", ")[2].substring(10, s.split(", ")[2].length() - 1)); } catch (NullPointerException | IllegalArgumentException  e) { this.color = null; }
            try { this.nationality = Country.valueOf(s.split(", ")[3].substring(16, s.split(", ")[3].length() - 1)); } catch (NullPointerException | IllegalArgumentException  e) { this.nationality = null; }
        } catch (ArrayIndexOutOfBoundsException e) {}
    }

}


