CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255)        NOT NULL,
    full_name   VARCHAR(255)        NOT NULL,
    role        VARCHAR(50)         NOT NULL,
    created_at  TIMESTAMP           NOT NULL DEFAULT now()
);

CREATE TABLE projects (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id    BIGINT       NOT NULL REFERENCES users(id),
    status      VARCHAR(50)  NOT NULL,
    start_date  DATE,
    end_date    DATE,
    created_at  TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE tasks (
    id           BIGSERIAL PRIMARY KEY,
    project_id   BIGINT       NOT NULL REFERENCES projects(id),
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    status       VARCHAR(50)  NOT NULL,
    assignee_id  BIGINT REFERENCES users(id),
    due_date     DATE,
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE comments (
    id          BIGSERIAL PRIMARY KEY,
    task_id     BIGINT       NOT NULL REFERENCES tasks(id),
    author_id   BIGINT       NOT NULL REFERENCES users(id),
    content     TEXT         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE activities (
    id           BIGSERIAL PRIMARY KEY,
    entity_type  VARCHAR(50) NOT NULL,
    entity_id    BIGINT      NOT NULL,
    action       VARCHAR(100) NOT NULL,
    actor_id     BIGINT REFERENCES users(id),
    details      TEXT,
    created_at   TIMESTAMP   NOT NULL DEFAULT now()
);
