package models;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Movies")
@NamedQueries({
        @NamedQuery(name = "Movie.getMovieByTitle",
                query = "SELECT m FROM Movie m WHERE m.title = :title"),
        @NamedQuery(name = "Movie.getActorsByMovieName",
                query = "SELECT ma.actor FROM MovieActor ma WHERE ma.movie.title = :movieName"),
        @NamedQuery(name = "Movie.getTopRatedMovies",
                query = "SELECT m FROM Movie m ORDER BY m.rating DESC"),
        @NamedQuery(name = "Movie.deleteMoviesOlderThan",
                query = "DELETE FROM Movie m WHERE m.releaseDate < :date"),
        @NamedQuery(name = "Movie.getAllMovies",
                query = "SELECT m FROM Movie m")
})
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private Date releaseDate;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private float rating;

    @ManyToOne
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Person director;

    public Movie() {
    }

    public Movie(String title, Date releaseDate, String country, float rating, Person director) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.country = country;
        this.rating = rating;
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Person getDirector() {
        return director;
    }

    public void setDirector(Person director) {
        this.director = director;
    }
}