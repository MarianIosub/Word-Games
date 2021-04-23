CREATE TABLE words (
    id INTEGER,
    text VARCHAR(50),
    CONSTRAINT words_pk PRIMARY KEY(id)
);

COMMIT;

-- SELECT * FROM words;
-- SELECT * FROM words WHERE id=1;
-- SELECT count(*) FROM words;