DROP TABLE IF EXISTS stat;
CREATE TABLE IF NOT EXISTS stat
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY not null
        CONSTRAINT stat_pk
            PRIMARY KEY,
    app       varchar(512),
    uri       varchar(512),
    ip        varchar(512),
    timestamp timestamp
);