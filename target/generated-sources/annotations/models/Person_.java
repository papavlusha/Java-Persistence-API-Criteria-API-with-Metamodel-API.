package models;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.sql.Date;

@Generated(value="EclipseLink-4.0.1.v20230224-rd91fc33f4c07cccdc5ab1be2004b8690ed8e81da", date="2023-12-14T17:15:57")
@StaticMetamodel(Person.class)
@SuppressWarnings({"rawtypes", "deprecation"})
public class Person_ { 

    public static volatile SingularAttribute<Person, String> fullName;
    public static volatile SingularAttribute<Person, Integer> id;
    public static volatile SingularAttribute<Person, Date> birthDate;

}