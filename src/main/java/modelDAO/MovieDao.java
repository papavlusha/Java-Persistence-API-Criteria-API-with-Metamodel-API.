package modelDAO;

import connection.ConnectionPool;
import connection.ConnectionPoolException;
import jakarta.persistence.*;
import models.Movie;
import models.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.Movie_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import models.Person_;

import static services.LoggerManager.logException;

public class MovieDao {

    private EntityManager entityManager;

    public MovieDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addMovie(Movie movie) {
        EntityTransaction et = entityManager.getTransaction();
        try {
            et.begin();
            entityManager.persist(movie);
            et.commit();
        } catch (Exception ex) {
            if (et != null && et.isActive()) {
                et.rollback();
            }
            throw ex;
        }
    }

    public Movie getMovieByTitle(String title) throws DAOException {
        EntityManager em = null;
        Movie movie = null;
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> rootEntry = cq.from(Movie.class);
            Predicate apartmentIdPredicate = cb.equal(rootEntry.get(Movie_.title), title);
            CriteriaQuery<Movie> apartmentQuery = cq.select(rootEntry).where(apartmentIdPredicate);
            TypedQuery<Movie> query = em.createQuery(apartmentQuery);
            movie = query.getSingleResult();

            if (movie == null)
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
        return movie;
//        TypedQuery<Movie> query = entityManager.createNamedQuery("Movie.getMovieByTitle", Movie.class);
//        query.setParameter("title", title);
//        List<Movie> movies = query.getResultList();
//        return movies.isEmpty() ? null : movies.get(0);
    }

    public List<Person> getActorsByMovieName(String movieName) {
        TypedQuery<Person> query = entityManager.createNamedQuery("Movie.getActorsByMovieName", Person.class);
        query.setParameter("movieName", movieName);
        return query.getResultList();
    }

    public List<Movie> getTopRatedMovies(int limit) throws DAOException {
        EntityManager em = null;
        List<Movie> movie = new ArrayList<>();
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);

            Root<Movie> root = cq.from(Movie.class);

            cq.select(root);
            cq.orderBy(cb.desc(root.get("rating"))); // Замените "rating" на имя поля вашей сущности Movie, по которому вы хотите сортировать

            TypedQuery<Movie> query = entityManager.createQuery(cq).setMaxResults(limit);
            movie = query.getResultList();

            if (movie == null)
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

        return movie;

//        TypedQuery<Movie> query = entityManager.createNamedQuery("Movie.getTopRatedMovies", Movie.class);
//        query.setMaxResults(limit);
//        return query.getResultList();
    }

    public void deleteMoviesOlderThan(Date dateThreshold) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Query query = entityManager.createNamedQuery("Movie.deleteMoviesOlderThan");
            query.setParameter("date", dateThreshold);
            query.executeUpdate();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Movie> getAllMovies() throws DAOException {
        EntityManager em = null;
        List<Movie> movie = new ArrayList<>();
        try {
            em = ConnectionPool.getConnection();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> rootEntry = cq.from(Movie.class);
            CriteriaQuery<Movie> apartmentQuery = cq.select(rootEntry) ;
            TypedQuery<Movie> query = em.createQuery(apartmentQuery);
            movie = query.getResultList();

            if (movie == null)
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

        TypedQuery<Movie> query = entityManager.createNamedQuery("Movie.getAllMovies", Movie.class);
        return query.getResultList();
    }
}