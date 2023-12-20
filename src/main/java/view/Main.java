package view;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import modelDAO.MovieActorDao;
import modelDAO.MovieDao;
import modelDAO.PersonDao;
import models.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import static services.LoggerManager.logException;

public class Main {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("videoLibrary");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    private static MovieDao movieDao = null;
    private static PersonDao personDao = null;
    private static MovieActorDao movieActorDao = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        try {
            entityManager = entityManagerFactory.createEntityManager();

            movieDao = new MovieDao(entityManager);
            personDao = new PersonDao(entityManager);
            movieActorDao = new MovieActorDao(entityManager);

            while (true) {
                System.out.println("Выберите действие:");
                System.out.println("1. Вывести все фильмы");
                System.out.println("2. Вывести информацию об актерах, снимавшихся в заданном фильме");
                System.out.println("3. Вывести информацию о фильмах с наивысшим рейтингом");
                System.out.println("4. Вывести информацию об актерах, снимавшихся как минимум в N фильмах");
                System.out.println("5. Вывести информацию об актерах, которые были режиссерами хотя бы одного из фильмов");
                System.out.println("6. Добавить новый фильм");
                System.out.println("7. Удалить все фильмы, дата выхода которых была более заданного числа лет назад");
                System.out.println("8. Добавить человека");
                System.out.println("9. Добавить актёрский состав");
                System.out.println("0. Выход\n");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        showAllMovies();//done
                        break;
                    case "2":
                        showActorsOfFilm();//done
                        break;
                    case "3":
                        showTopRatedMovies();//done
                        break;
                    case "4":
                        showActorsInNPlusFilms();//done
                        break;
                    case "5":
                        showActorDirectors();//
                        break;
                    case "6":
                        addMovie();//done
                        break;
                    case "7":
                        deleteOldFilms();//done
                        break;
                    case "8":
                        addPerson();//works
                        break;
                    case "9":
                        addCast();//works
                        break;
                    case "0":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте еще раз.");
                }
            }
        }
        finally {
            if(entityManager != null) {
                entityManager.close();
            }
            entityManagerFactory.close();
        }
    }

    // case 1
    private static void showAllMovies()  {
        try {
            System.out.println("Список всех фильмов:");
            List<Movie> movies = movieDao.getAllMovies();
            for (Movie movie : movies) {
                System.out.println(movie.getTitle());
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }
    }

    // case 2
    private static void showActorsOfFilm() {
        try {
            System.out.println("Введите название фильма:");
            String filmTitle = scanner.nextLine();
            List<Person> actors = movieDao.getActorsByMovieName(filmTitle);
            for (Person actor : actors) {
                System.out.println(actor.getFullName());
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }
    }

    // case 3
    private static void showTopRatedMovies() throws Exception {
        try {
            System.out.println("Введите количество фильмов для отображения:");
            int limit = Integer.parseInt(scanner.nextLine());
            List<Movie> movies = movieDao.getTopRatedMovies(limit);
            for (Movie movie : movies) {
                System.out.print(movie.getTitle());
                System.out.print(": ");
                System.out.println(movie.getRating());
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);

        }
    }

    // case 4
    private static void showActorsInNPlusFilms() {
        try {
            System.out.println("Введите минимальное количество фильмов, в которых снимался актер:");
            int minMovies = Integer.parseInt(scanner.nextLine());
            List<Person> actors = personDao.getActorsInNPlusFilms(minMovies);
            for (Person actor : actors) {
                System.out.println(actor.getFullName());
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }
    }
    
    // case 5
    private static void showActorDirectors() throws Exception {
        try {
            List<Person> people = personDao.getActorsWhoDirected();
            for (Person person : people) {
                System.out.println(person.getFullName());
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }

    }

    // case 6

    private static void addMovie() throws Exception {
        try {
            System.out.println("Введите название фильма:");
            String title = scanner.nextLine();
            System.out.println("Введите дату выхода фильма (формат: yyyy-mm-dd):");
            Date releaseDate = Date.valueOf(scanner.nextLine());
            System.out.println("Введите страну производства:");
            String country = scanner.nextLine();
            System.out.println("Введите рейтинг фильма:");
            float rating = Float.parseFloat(scanner.nextLine());
            System.out.println("Введите имя режиссера:");
            String directorName = scanner.nextLine();
            Person director = personDao.getPersonByName(directorName);

            if (director == null) {
                System.out.println("В базе данных не найдено такого человека.\nНажмите 1, если желаете добавить его в базу данных.");
                String choice = scanner.nextLine();
                if ("1".equals(choice)) {
                    System.out.println("Введите дату рождения (формат: yyyy-mm-dd):");
                    Date birthDate = Date.valueOf(scanner.nextLine());
                    Person person = new Person(directorName, birthDate);
                    personDao.addPerson(person);
                    Movie movie = new Movie(title, releaseDate, country, rating, person);
                    movieDao.addMovie(movie);
                    System.out.println("Фильм успешно добавлен.");
                    addCastToMovie(movie);
                } else {
                    System.out.println("Не удалось найти режиссера. Создание фильма отменено.");
                    return;
                }
            }
            else {
                Movie movie = new Movie(title, releaseDate, country, rating, director);
                movieDao.addMovie(movie);
                System.out.println("Фильм успешно добавлен.");
                addCastToMovie(movie);
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }

    }

    // case 7

    private static void deleteOldFilms() throws Exception {
        try {
            System.out.println("Введите количество лет, фильмы старше которого следует удалить:");
            int years = Integer.parseInt(scanner.nextLine());

            Date dateThreshold = Date.valueOf(LocalDate.now().minusYears(years));
            movieDao.deleteMoviesOlderThan(dateThreshold);
            System.out.println("Старые фильмы успешно удалены.");
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }

    }

    // case 8
    public static void addPerson() throws Exception {
        try {
            System.out.println("Введите имя:");
            String name = scanner.nextLine();
            System.out.println("Введите дату рождения (формат: yyyy-mm-dd):");
            Date birthDate = Date.valueOf(scanner.nextLine());

            Person person = new Person(name, birthDate);
            personDao.addPerson(person);
            System.out.println("Человек успешно добавлен.");
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }

    }

    //case 9
    private static void addCast() throws Exception {
        try {
            System.out.println("Введите название фильма к которому хотите добавить актёрский состав: ");
            String title = scanner.nextLine();
            Movie movie = movieDao.getMovieByTitle(title);

            if (movie == null) {
                System.out.println("В базе данных нет такого фильма, добавьте его через соответсвующий пункт меню.");
                return;
            }

            addCastToMovie(movie);
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }

    }

    private static void addCastToMovie(Movie movie) throws Exception {
        try {
            System.out.println("Сколько актёров вы собираетесь добавить? ");
            int limit = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < limit; i++) {
                System.out.println("Введите имя актёра: ");
                String name = scanner.nextLine();
                Person actor = personDao.getPersonByName(name);

                if (actor == null) {
                    System.out.println("Нет такого актёра. Чтобы добавить нажмите 1.");
                    String choice = scanner.nextLine();
                    if ("1".equals(choice)) {
                        System.out.println("Введите дату рождения (формат: yyyy-mm-dd):");
                        Date birthDate = Date.valueOf(scanner.nextLine());
                        Person person = new Person(name, birthDate);
                        personDao.addPerson(person);
                        movieActorDao.addCast(movie, person);
                    } else {
                        continue;
                    }
                    continue;
                }
                movieActorDao.addCast(movie, actor);
            }
        } catch (Exception e) {
            System.out.println("Вызвано исключение");
            logException(e);
        }

    }
}