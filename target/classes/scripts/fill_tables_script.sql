USE videoLibrary;

INSERT INTO People (full_name, birth_date)
VALUES
    ('Christopher Nolan', '1970-07-30'),
    ('Quentin Tarantino', '1963-03-27'),
    ('Emma Stone', '1988-11-06');

INSERT INTO Movies (title, release_date, country, rating, director_id)
VALUES
    ('Inception', '2010-07-16', 'USA', 8.8, 1),
    ('Pulp Fiction', '1994-10-14', 'USA', 8.9, 2),
    ('La La Land', '2016-12-09', 'USA', 8.0, 3);


INSERT INTO Movies_Actors (movie_id, actor_id)
VALUES
    (1, 1),
    (1, 3),
    (2, 2),
    (2, 3),
    (3, 3),
    (3, 2);
