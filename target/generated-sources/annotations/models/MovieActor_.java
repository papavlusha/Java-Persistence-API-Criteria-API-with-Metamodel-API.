package models;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import models.Movie;
import models.Person;

@Generated(value="EclipseLink-4.0.1.v20230224-rd91fc33f4c07cccdc5ab1be2004b8690ed8e81da", date="2023-12-14T17:15:57")
@StaticMetamodel(MovieActor.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class MovieActor_ { 

    public static volatile SingularAttribute<MovieActor, Person> actor;
    public static volatile SingularAttribute<MovieActor, Movie> movie;

}