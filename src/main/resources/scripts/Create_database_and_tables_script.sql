
USE videoLibrary;

CREATE TABLE Movies(
    id INT AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    country VARCHAR(255) NOT NULL,
    rating FLOAT NOT NULL,
    director_id INT,

    FOREIGN KEY (director_id) REFERENCES People(id)
);

CREATE TABLE People(
    id INT AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Movies_Actors(
    movie_id INT,
    actor_id INT,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES Movies(id),
    FOREIGN KEY (actor_id) REFERENCES People(id)
);