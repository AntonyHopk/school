CREATE TABLE IF NOT EXISTS users_auth
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(50)  NOT NULL,
    is_blocked    BOOLEAN      NOT NULL DEFAULT false,
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES  users_auth(id) ON DELETE CASCADE ,
    token_hash VARCHAR(255) NOT NULL UNIQUE ,
    expires_at TIMESTAMP NOT NULL ,
    revoke_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT  now()
);
CREATE INDEX  idx_refresh_user_id ON refresh_tokens(user_id);