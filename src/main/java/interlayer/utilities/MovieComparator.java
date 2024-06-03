package interlayer.utilities;

import interlayer.models.Movie;

import java.util.Comparator;

public class MovieComparator implements Comparator<Movie> {
    public int compare(Movie a, Movie b){
        return a.getGenre().compareTo(b.getGenre());
    }
}