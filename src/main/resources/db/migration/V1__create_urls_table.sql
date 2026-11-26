CREATE TABLE urls (
                      id UUID PRIMARY KEY,

                      short_code VARCHAR(10) NOT NULL UNIQUE,

                      original_url VARCHAR(2048) NOT NULL,

                      created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                      expires_at TIMESTAMPTZ,

                      click_count BIGINT NOT NULL DEFAULT 0,

                      is_active BOOLEAN NOT NULL DEFAULT TRUE
);