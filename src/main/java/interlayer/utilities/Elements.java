package interlayer.utilities;

/**
 * Абстрактный класс элементов коллекции.
 */
public abstract class Elements implements Validatable, Comparable<Elements>{
    abstract public long getId();
}
