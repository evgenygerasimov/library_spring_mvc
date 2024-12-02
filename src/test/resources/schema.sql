CREATE TABLE IF NOT EXISTS author (author_id SERIAL PRIMARY KEY, author_name VARCHAR(255));

CREATE TABLE IF NOT EXISTS books (id SERIAL PRIMARY KEY, genre VARCHAR(255),
    title VARCHAR(255), author_id int, FOREIGN KEY (author_id) REFERENCES author(author_id));