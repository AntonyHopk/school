CREATE TABLE IF NOT EXISTS user_profiles(
    id BIGINT PRIMARY KEY ,
    username VARCHAR(50) NOT NULL UNIQUE ,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL ,
    bio VARCHAR(500),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()

);
CREATE INDEX IF NOT EXISTS  idx_user_profiles_username ON user_profiles(username);

CREATE TABLE IF NOT EXISTS consumed_events(
    event_id UUID PRIMARY KEY ,
    event_type VARCHAR(100) NOT NULL ,
    consumed_at TIMESTAMP NOT NULL DEFAULT now()
);