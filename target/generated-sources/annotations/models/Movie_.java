package models;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Date;
import models.Person;

@Generated(value="EclipseLink-4.0.1.v20230224-rd91fc33f4c07cccdc5ab1be2004b8690ed8e81da", date="2023-12-14T17:15:57")
@StaticMetamodel(Movie.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class Movie_ { 

    public static volatile SingularAttribute<Movie, String> country;
    public static volatile SingularAttribute<Movie, Date> releaseDate;
    public static volatile SingularAttribute<Movie, Person> director;
    public static volatile SingularAttribute<Movie, Float> rating;
    public static volatile SingularAttribute<Movie, Integer> id;
    public static volatile SingularAttribute<Movie, String> title;

}