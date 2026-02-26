CREATE TABLE IF NOT EXISTS courses
(
    id            BIGSERIAL PRIMARY KEY,
    owner_user_id BIGINT       NOT NULL,
    title         VARCHAR(200) NOT NULL,
    description   VARCHAR(500) NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_courses_owner ON courses (owner_user_id);
CREATE INDEX IF NOT EXISTS idx_courses_status ON courses (status);

CREATE TABLE IF NOT EXISTS course_modules
(
    id         BIGSERIAL PRIMARY KEY,
    course_id  BIGINT       NOT NULL,
    title      VARCHAR(200) NOT NULL,
    position   INT          NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT fk_modules_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT uq_modules_courses_position UNIQUE (course_id, position)
);
CREATE INDEX IF NOT EXISTS idx_modules_course ON course_modules (course_id);

CREATE TABLE IF NOT EXISTS lessons
(
    id         BIGSERIAL PRIMARY KEY,
    module_id  BIGINT       NOT NULL,
    title      VARCHAR(200) NOT NULL,
    content    TEXT,
    position   INT          NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT fk_lessons_module FOREIGN KEY (module_id) REFERENCES course_modules (id) ON DELETE CASCADE,
    CONSTRAINT uq_lessons_module_position UNIQUE (module_id, position)

);

CREATE INDEX IF NOT EXISTS idx_lessons_module ON lessons (module_id);