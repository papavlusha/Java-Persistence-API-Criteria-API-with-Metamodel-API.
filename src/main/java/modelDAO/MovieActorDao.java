package modelDAO;

import jakarta.persistence.*;
import models.Movie;
import models.MovieActor;
import models.Person;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class MovieActorDao {

    private EntityManager entityManager;

    public MovieActorDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addCast(Movie movie, Person actor) {
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            MovieActor movieActor = new MovieActor(movie, actor);
            entityManager.persist(movieActor);
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                et.rollback();
            }
            throw ex;
        }
    }
}
