--liquibase formatted sql

--changeset DanielK:1
CREATE TABLE IF NOT EXISTS users
(
    id              SERIAL PRIMARY KEY,
    email           TEXT NOT NULL,
    password        TEXT NOT NULL,
    updated         TIMESTAMP,
    created         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles
(
    id              SERIAL PRIMARY KEY,
    authority       TEXT NOT NULL,
    updated         TIMESTAMP,
    created         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id         BIGINT NOT NULL,
    role_id         BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS tasks
(
    id              SERIAL PRIMARY KEY,
    tittle          TEXT NOT NULL,
    description     TEXT,
    status          TEXT NOT NULL,
    task_priority   TEXT,
    author_id       BIGINT,
    executor_id     BIGINT,
    updated         TIMESTAMP,
    created         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (executor_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id              SERIAL PRIMARY KEY,
    content         TEXT NOT NULL,
    author_id       BIGINT,
    task_id         BIGINT,
    updated         TIMESTAMP,
    created         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks(status);
CREATE INDEX IF NOT EXISTS idx_tasks_priority ON tasks(task_priority);
CREATE INDEX IF NOT EXISTS idx_comments_task_id ON comments(task_id);


/* CREATE FUNCTION create_update() RETURNS trigger
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        NEW.updated := NOW();
        NEW.created := OLD.created;
ELSE
        NEW.created := NOW();
        NEW.updated := null;
END IF;
RETURN NEW;
END;
$$ /


CREATE TRIGGER trg_user_create_update
    BEFORE INSERT OR UPDATE
                         ON users
                         FOR EACH ROW
                         EXECUTE PROCEDURE create_update() /

CREATE TRIGGER role_create_update
    BEFORE INSERT OR UPDATE
                         ON roles
                         FOR EACH ROW
                         EXECUTE PROCEDURE create_update() /

CREATE TRIGGER user_role_create_update
    BEFORE INSERT OR UPDATE
                         ON user_roles
                         FOR EACH ROW
                         EXECUTE PROCEDURE create_update() /

CREATE TRIGGER task_create_update
    BEFORE INSERT OR UPDATE
                         ON tasks
                         FOR EACH ROW
                         EXECUTE PROCEDURE create_update() /

CREATE TRIGGER comment_create_update
    BEFORE INSERT OR UPDATE
                         ON comments
                         FOR EACH ROW
                         EXECUTE PROCEDURE create_update() /*/