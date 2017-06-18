CREATE TABLE rss_feed (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  url         VARCHAR(200) UNIQUE               NOT NULL,
  link        VARCHAR(200)                      NOT NULL,
  title       VARCHAR(300)                      NOT NULL,
  description VARCHAR(1000)                      NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);

CREATE TABLE rss_item (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  title       VARCHAR(300),
  link        VARCHAR(200),
  description TEXT,
  td_length   DOUBLE NOT NULL,
  feed_id     BIGINT NOT NULL REFERENCES rss_feed (id),
  pub_time    TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);

CREATE TABLE term (
  value              VARCHAR(200) PRIMARY KEY NOT NULL,
  document_frequency BIGINT                   NOT NULL DEFAULT 0
);

CREATE TABLE rss_item_terms (
  rss_item_id BIGINT REFERENCES rss_item (id),
  term_value  VARCHAR(200) REFERENCES term (value),
  usages      INT NOT NULL,
  CONSTRAINT rss_item_term_value_unique UNIQUE (rss_item_id, term_value)
);
