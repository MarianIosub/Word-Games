-- conn sys as sysdba

create tablespace word_games datafile 'word_games.ora' size 200m;

create user usrwordgames identified by pwdwordgames default tablespace word_games temporary tablespace temp;

ALTER USER usrwordgames QUOTA 200M ON word_games;

GRANT CONNECT TO usrwordgames;
GRANT CREATE TABLE TO usrwordgames;
GRANT CREATE VIEW TO usrwordgames;
GRANT CREATE SEQUENCE TO usrwordgames;
GRANT CREATE TRIGGER TO usrwordgames;
GRANT CREATE SYNONYM TO usrwordgames;
GRANT CREATE PROCEDURE TO usrwordgames;
GRANT CREATE TYPE TO usrwordgames;

COMMIT;

-- CONN usrwordgames/pwdwordgames@localhost/XE