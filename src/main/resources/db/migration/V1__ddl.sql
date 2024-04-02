CREATE TABLE IF NOT EXISTS users
(
    id                  UUID DEFAULT gen_random_uuid() UNIQUE,
    email               CHARACTER VARYING(100) NOT NULL,
    "password"          CHARACTER VARYING(150) NOT NULL,
    first_name          CHARACTER VARYING(100),
    last_name           CHARACTER VARYING(100),
    status              CHARACTER VARYING(100),
    subject             CHARACTER VARYING(30),
    "level"             CHARACTER VARYING(30),
    goal                CHARACTER VARYING(30),
    phone               VARCHAR(255),
    address             VARCHAR(255),
    about_yourself      TEXT,
    is_active           BOOLEAN                NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS token_links
(
    id                  UUID DEFAULT gen_random_uuid() UNIQUE,
    token               CHARACTER VARYING(1000) NOT NULL,
    active_time         BIGINT,
    create_time         TIMESTAMP,
    is_active           BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS courses
(
    id                  UUID DEFAULT gen_random_uuid() UNIQUE,
    subject             CHARACTER VARYING(30)
);

CREATE TABLE IF NOT EXISTS orders
(
    id                  UUID DEFAULT gen_random_uuid() UNIQUE,
    student_id          UUID,
    tutor_id            UUID,
    subject             CHARACTER VARYING(30),
    state               CHARACTER VARYING(30),
    start_date          TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES users (id),
    FOREIGN KEY (tutor_id) REFERENCES users (id)
);
