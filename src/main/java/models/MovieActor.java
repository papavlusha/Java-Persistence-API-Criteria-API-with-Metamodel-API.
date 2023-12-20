package models;

import jakarta.persistence.*;

@Entity
@Table(name = "Movies_Actors")
public class MovieActor {

    @Id
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id", referencedColumnName = "id")
    private Person actor;

    public MovieActor() {
    }

    public MovieActor(Movie movie, Person actor) {
        this.movie = movie;
        this.actor = actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Person getActor() {
        return actor;
    }

    public void setActor(Person actor) {
        this.actor = actor;
    }
}
