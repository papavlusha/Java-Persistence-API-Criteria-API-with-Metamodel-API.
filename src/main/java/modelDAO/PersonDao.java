package modelDAO;

import connection.ConnectionPool;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import models.*;
import connection.ConnectionPoolException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static models.MovieActor_.movie;
import static services.LoggerManager.logException;

public class PersonDao {

    private EntityManager entityManager;

    public PersonDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addPerson(Person person) throws DAOException {
//        EntityTransaction et = entityManager.getTransaction();
//        try {
//            et.begin();
//            entityManager.persist(person);
//            et.commit();
//        } catch (Exception ex) {
//            if (et != null && et.isActive()) {
//                et.rollback();
//            }
//            throw ex;
//        }
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = ConnectionPool.getConnection();
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                throw new DAOException("Creating apartment failed, no rows affected");
            }
            throw new DAOException(e);
        } finally {
            if (em != null) {
                try {
                    ConnectionPool.releaseConnection(em);
                } catch(ConnectionPoolException e) {
                    logException(e);
                }
            }
        }

    }

    public Person getPersonByName(String name) throws DAOException {
        EntityManager em = null;
        Person person = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Person> cq = cb.createQuery(Person.class);
            Root<Person> rootEntry = cq.from(Person.class);
            Predicate apartmentIdPredicate = cb.equal(rootEntry.get(Person_.fullName), name);
            CriteriaQuery<Person> apartmentQuery = cq.select(rootEntry).where(apartmentIdPredicate);
            TypedQuery<Person> query = em.createQuery(apartmentQuery);
            person = query.getSingleResult();

            if (person == null)
                throw new SQLException("Receiving person failed, no person found");
        } catch (Exception e) {
            throw new DAOException(e);
        } finally {
            if (em != null) {
                try {
                    ConnectionPool.releaseConnection(em);
                } catch(ConnectionPoolException e) {
                    logException(e);
                }
            }
        }
        return person;
//        TypedQuery<Person> query = entityManager.createNamedQuery("Person.getPersonByName", Person.class);
//        query.setParameter("name", name);
//        List<Person> persons = query.getResultList();
//        return persons.isEmpty() ? null : persons.get(0);
    }

    public List<Person> getActorsInNPlusFilms(int minMovies) throws DAOException {
        TypedQuery<Person> query = entityManager.createNamedQuery("Person.getActorsInNPlusFilms", Person.class);
        query.setParameter("minMovies", minMovies);
        return query.getResultList();
//        EntityManager em = null;
//        List<Person> actors = new ArrayList<>();
//        try {
//            em = ConnectionPool.getConnection();
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Person> cq = cb.createQuery(Person.class);

////            Root<MovieActor> root = cq.from(MovieActor.class);
////            Path<Person> actorPath = root.get(MovieActor_.actor);
////
////            cq.select(actorPath);
////            cq.groupBy(actorPath);
////            cq.having(cb.greaterThanOrEqualTo(cb.count(root.get(MovieActor_.movie)), (long)minMovies));
////
////            TypedQuery<Person> query = em.createQuery(cq);
////            actors = query.getResultList();
//            Root<MovieActor> movieActorRoot = cq.from(MovieActor.class);
//            Join<MovieActor, Person> personJoin = movieActorRoot.join(MovieActor_.actor);
//            Join<MovieActor, Movie> movieJoin = movieActorRoot.join(MovieActor_.movie);
//
//            cq.select(personJoin);
//            cq.groupBy(personJoin.get(Person_.id));
//            cq.having(cb.gt(cb.count(movieJoin), (long)minMovies));
//            TypedQuery<Person> query = em.createQuery(cq);
//            actors = query.getResultList();
//            if (actors == null)
//                throw new SQLException("Receiving person failed, no person found");
//        } catch (Exception e) {
//            throw new DAOException(e);
//        } finally {
//            if (em != null) {
//                try {
//                    ConnectionPool.releaseConnection(em);
//                } catch(ConnectionPoolException e) {
//                    logException(e);
//                }
//            }
//        }
//
//        return actors;
    }

    public List<Person> getActorsWhoDirected() throws DAOException {
//        TypedQuery<Person> query = entityManager.createNamedQuery("Person.getActorsWhoDirected", Person.class);
//        return query.getResultList();

        EntityManager em = null;
        List<Person> actors = new ArrayList<>();
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Person> cq = cb.createQuery(Person.class);

            Subquery<Person> subquery = cq.subquery(Person.class);
            Root<Movie> subRoot = subquery.from(Movie.class);
            Join<Movie, Person> directorJoin = subRoot.join("director");

            subquery.select(directorJoin);

            Subquery<Person> actorsSubquery = subquery.subquery(Person.class);
            Root<MovieActor> actorsRoot = actorsSubquery.from(MovieActor.class);
            actorsSubquery.select(actorsRoot.get("actor"));

            subquery.where(directorJoin.in(actorsSubquery));

            cq.select(subquery.getSelection().alias("director")).distinct(true);

            TypedQuery<Person> query = entityManager.createQuery(cq);
            actors = query.getResultList();
            if (actors == null)
                throw new SQLException("Receiving person failed, no person found");
        } catch (Exception e) {
            throw new DAOException(e);
        } finally {
            if (em != null) {
                try {
                    ConnectionPool.releaseConnection(em);
                } catch(ConnectionPoolException e) {
                    logException(e);
                }
            }
        }

        return actors;
    }
}