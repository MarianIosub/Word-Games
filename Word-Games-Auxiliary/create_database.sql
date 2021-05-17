DROP TABLE fazan_scores;
DROP TABLE hangman_scores;
DROP TABLE typefast_scores;
DROP TABLE words;
DROP TABLE users;

CREATE TABLE words (
    id INTEGER,
    text VARCHAR(50),
    CONSTRAINT words_pk PRIMARY KEY(id)
);

DROP INDEX word_text_index;
CREATE INDEX word_text_index ON WORDS(text);
--SELECT * FROM words WHERE text='mama';

DROP INDEX word_text_start_pattern_index;
CREATE INDEX word_text_start_pattern_index ON WORDS(substr(text, 1, 1));
--SELECT * FROM words WHERE text LIKE 're%';
--SELECT * FROM words WHERE substr(text, 1, 2) = 're';


CREATE TABLE users (
    id INTEGER,
    username VARCHAR(100),
    password VARCHAR(100),
    CONSTRAINT users_pk PRIMARY KEY(id)
);

CREATE TABLE fazan_scores (
    id INTEGER,
    user_id INTEGER,
    score INTEGER,
    CONSTRAINT fazan_scores_pk PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE hangman_scores (
    id INTEGER,
    user_id INTEGER,
    score INTEGER,
    CONSTRAINT hangman_scores_pk PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE typefast_scores (
    id INTEGER,
    user_id INTEGER,
    score INTEGER,
    CONSTRAINT typefast_scores_pk PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE fazan_scores MODIFY user_id INTEGER UNIQUE NOT NULL;
ALTER TABLE hangman_scores MODIFY user_id INTEGER UNIQUE NOT NULL;
ALTER TABLE typefast_scores MODIFY user_id INTEGER UNIQUE NOT NULL;

COMMIT;

-- SELECT * FROM words;
-- SELECT * FROM words WHERE id=1;
-- SELECT count(*) FROM words;

-- select * from words where id = 166920;