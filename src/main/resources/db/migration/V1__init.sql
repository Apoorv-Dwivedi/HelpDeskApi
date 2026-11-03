CREATE TABLE app_user (
                          id          BIGSERIAL PRIMARY KEY,
                          google_sub  VARCHAR(64)  NOT NULL UNIQUE,
                          email       VARCHAR(255) NOT NULL UNIQUE,
                          name        VARCHAR(255),
                          role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
                          created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);