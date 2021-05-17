DROP TABLE words;
DROP TABLE users;

CREATE TABLE words (
    id INTEGER,
    text VARCHAR(50),
    CONSTRAINT words_pk PRIMARY KEY(id)
);

CREATE INDEX word_text_index ON WORDS(text);
-- DROP INDEX word_text_index;
--SELECT * FROM words WHERE text='mama';

CREATE INDEX word_text_start_pattern_index ON WORDS(substr(text, 1, 1));
-- DROP INDEX word_text_start_pattern_index;
SELECT * FROM words WHERE text LIKE 're%';
SELECT * FROM words WHERE substr(text, 1, 2) = 're';


CREATE TABLE users (
    id INTEGER,
    username VARCHAR(100),
    password VARCHAR(100),
    CONSTRAINT users_pk PRIMARY KEY(id)
);

COMMIT;

-- SELECT * FROM words;
-- SELECT * FROM words WHERE id=1;
-- SELECT count(*) FROM words;

-- select * from words where id = 166920;