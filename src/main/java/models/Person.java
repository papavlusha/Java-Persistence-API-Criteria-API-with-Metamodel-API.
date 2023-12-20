package models;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "People")
@NamedQueries({
        @NamedQuery(name = "Person.getPersonByName",
                query = "SELECT p FROM Person p WHERE p.fullName = :name"),
        @NamedQuery(name = "Person.getActorsInNPlusFilms",
                query = "SELECT ma.actor FROM MovieActor ma GROUP BY ma.actor HAVING COUNT(ma.movie) >= :minMovies"),
        @NamedQuery(name = "Person.getActorsWhoDirected",
                query = "SELECT DISTINCT m.director FROM Movie m WHERE m.director IN (SELECT ma.actor FROM MovieActor ma)")
})

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    public Person() {
    }

    public Person(String fullName, Date birthDate) {
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}