package client.utilities;

import interlayer.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

/**
 * Класс чтения объекта c консоли, проверка на корректность ввода
 */
public class Check {
    public static class RequestBreak extends Exception {};

    public static Movie RequestMovie(StandartConsole console) throws RequestBreak {
        try {
            String name;
            long id;
            while (true) {
                id = 1;
                console.print("Название фильма: "); 
                name = console.readln().trim();
                if (name.equals("exit")) throw new RequestBreak();
                if (!name.isEmpty()) break;
                console.println("Неверный формат ввода. Повторите ввод:");
            }
            var coordinates = RequestCoordinates(console);
            LocalDateTime creathionDate = LocalDateTime.now();
            long oscarsCount;
            while (true) {
                console.print("Количество оскаров (число должно быть целым): ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new RequestBreak();
                if (!line.isEmpty()) {
                    try { oscarsCount = Long.parseLong(line); if (oscarsCount>0) break; }
                    catch (NumberFormatException e) { }
                }
            }
            var genre = RequestGenre(console);
            var mpaaRating = RequestMpaaRating(console);
            var director = RequestDirector(console);
            return new Movie(id, name, coordinates, creathionDate, oscarsCount, genre, mpaaRating, director);
        }
        catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }


    public static Coordinates RequestCoordinates(StandartConsole console) throws RequestBreak{
        try {
            long x;
            while(true){
                console.print("Координата x: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new RequestBreak();
                if (!line.isEmpty()) {
                    try { x = Math.round(Float.parseFloat(line)); break;}
                    catch (NumberFormatException e) { }
                }
            }
            int y;
            while(true) {
                console.print("Координата y: ");
                var line = console.readln().trim();
                if (line.equals("exit")) throw new RequestBreak();
                if (!line.isEmpty()){
                    try {y = Math.round(Float.parseFloat(line));if (y <= 72) break; }
                    catch (NumberFormatException e) { }
                }
            }
            return new Coordinates(x, y);
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }


    public static MovieGenre RequestGenre(StandartConsole console) throws RequestBreak{
        try {
            MovieGenre genre;
            while (true) {
                console.print("Жанр фильма ("+MovieGenre.names()+"): ");
                var line = console.readln().trim().toUpperCase();
                if (line.equals("exit")) throw new RequestBreak();
                if (!line.isEmpty()) {
                    try { genre = MovieGenre.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
                }
            }
            return genre;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }


    public static MpaaRating RequestMpaaRating(StandartConsole console) throws RequestBreak{
        try {
            MpaaRating mpaaRating;
            while (true) {
                console.print("Рейтинговая система MPAA : ("+MpaaRating.names()+"): ");
                var line = console.readln().trim().toUpperCase();
                if (line.equals("exit")) throw new RequestBreak();
                if (line.isEmpty()) { mpaaRating = null; break;}
                try { mpaaRating = MpaaRating.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
            }
            return mpaaRating;
        } catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }


    public static Person RequestDirector(StandartConsole console) throws RequestBreak{
        try {
            String name;
            while (true) {
                console.print("Имя режиcсёра: ");
                name = console.readln().trim();
                if (name.equals("exit")) throw new RequestBreak();
                if (!name.isEmpty()) break;
            }
            LocalDate birthday;
            while (true) {
                console.print("Дата рождения режиcсёра в формате YYYY-MM-DD: ");
                var line = console.readln().trim().replaceAll("[^\\d]", "-");
                if (line.equals("exit")) throw new RequestBreak();
                if (line.isEmpty()) { birthday = null; break;}
                try {birthday = LocalDate.parse(line, DateTimeFormatter.ISO_DATE);break;}
                catch (DateTimeParseException e) {}
                }
            Color color;
            while (true) {
                console.print("Цвет глаз режиcсёра: ("+Color.names()+"): ");
                var line = console.readln().trim().toUpperCase();
                if (line.equals("exit")) throw new RequestBreak();
                if (line.isEmpty()) { color = null; break;}
                try { color = Color.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
            }
            Country nationality;
            while (true) {
                console.print("Национальность режиcсёра: ("+Country.names()+"): ");
                var line = console.readln().trim().toUpperCase();
                if (line.equals("exit")) throw new RequestBreak();
                if (line.isEmpty()) { nationality = null; break;}
                try { nationality = Country.valueOf(line); break; } catch (NullPointerException | IllegalArgumentException  e) { }
            }
            return new Person(name, birthday, color, nationality);
        }
        catch (NoSuchElementException | IllegalStateException e) {
            console.printError("Ошибка чтения");
            return null;
        }
    }
}

