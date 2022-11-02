CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(320) NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(100),
    registration_date timestamp NOT NULL,
    state varchar(50),
    date_of_birthday varchar(20) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email));

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    url VARCHAR(1000) NOT NULL,
    item_state varchar(50),
    resolved_url varchar(1000),
    mime_type varchar(50),
    title varchar(1000),
    has_image boolean,
    has_video boolean,
    date_resolved TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    unread boolean,
    CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id), UNIQUE(id, url));

CREATE TABLE IF NOT EXISTS tags (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id BIGINT,
    name VARCHAR(50),
    CONSTRAINT fk_tags_to_items FOREIGN KEY(item_id) REFERENCES items(id));